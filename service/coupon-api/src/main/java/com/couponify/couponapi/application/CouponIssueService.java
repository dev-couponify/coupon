package com.couponify.couponapi.application;


import static com.couponify.couponapi.common.CouponPrefix.COUPON_INFO;
import static com.couponify.couponapi.common.CouponPrefix.COUPON_ISSUER;

import com.couponify.couponapi.exception.CouponErrorCode;
import com.couponify.couponapi.exception.CouponException;
import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.CouponCache;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import com.couponify.coupondomain.domain.issuedCoupon.IssuedCoupon;
import com.couponify.coupondomain.domain.issuedCoupon.repository.IssuedCouponRepository;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RTransaction;
import org.redisson.api.RedissonClient;
import org.redisson.api.TransactionOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "CouponIssueService")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponIssueService {

    private static final int QUANTITY_TO_ISSUE_COUPON = 1;
    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;
    private final RedissonClient redissonClient;

    @Value("${cache.coupon.expiration.hours}")
    private Long couponExpirationHours;

    public void cacheCouponIssuance(Long couponId, Long userId) {
        RTransaction transaction = redissonClient.createTransaction(TransactionOptions.defaults());
        try {
            CouponCache couponCache = getCouponCache(transaction, couponId);

            checkUserAlreadyIssued(transaction, couponId, userId);
            couponCache.issue(userId, QUANTITY_TO_ISSUE_COUPON);
            addIssuer(transaction, couponId, userId);
            updateCouponCache(transaction, couponId, couponCache);

            transaction.commit();

            setCouponCacheTTL(couponId, couponCache);
        } catch (Exception e) {
            transaction.rollback();
            throw new CouponException(CouponErrorCode.TRANSACTION_COMMIT_FAILED, e.getMessage());
        }
    }

    @Transactional
    public void persistCouponIssuance() {
        RTransaction transaction = redissonClient.createTransaction(TransactionOptions.defaults());
        try {
            RMap<Long, Set<Long>> couponIssuer = transaction.getMap(COUPON_ISSUER);
            if (couponIssuer.isEmpty()) {
                return;
            }
            processCouponIssuance(transaction, couponIssuer);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new CouponException(CouponErrorCode.TRANSACTION_COMMIT_FAILED, e.getMessage());
        }
    }

    public Set<Long> getIssuedCouponIds() {
        RMap<Long, Set<Long>> couponIssuer = redissonClient.getMap(COUPON_ISSUER);
        return couponIssuer.keySet();
    }

    private CouponCache getCouponCache(RTransaction transaction, Long couponId) {
        RMapCache<Long, CouponCache> couponInfo = transaction.getMapCache(COUPON_INFO);
        CouponCache couponCache = couponInfo.get(couponId);

        if (couponCache == null) {
            Coupon coupon = getCoupon(couponId);
            Set<Long> issuerIds = getIssuerIds(coupon);
            return CouponCache.of(coupon, issuerIds);
        }
        return couponCache;
    }

    private Set<Long> getIssuerIds(Coupon coupon) {
        return new HashSet<>(issuedCouponRepository.findUserIdsByCoupon(coupon));
    }

    private void checkUserAlreadyIssued(RTransaction transaction, Long couponId, Long userId) {
        RMap<Long, Set<Long>> couponIssuer = transaction.getMap(COUPON_ISSUER);
        Set<Long> issuers = couponIssuer.get(couponId);

        if (issuers == null) {
            issuers = new HashSet<>();
            couponIssuer.put(couponId, issuers);
        } else if (issuers.contains(userId)) {
            throw new CouponException(CouponErrorCode.COUPON_ALREADY_ISSUED);
        }
    }

    private void addIssuer(RTransaction transaction, Long couponId, Long userId) {
        RMap<Long, Set<Long>> couponIssuer = transaction.getMap(COUPON_ISSUER);
        Set<Long> issuers = couponIssuer.get(couponId);
        issuers.add(userId);
        couponIssuer.put(couponId, issuers);
    }

    private void updateCouponCache(RTransaction transaction, Long couponId,
        CouponCache couponCache) {
        RMapCache<Long, CouponCache> couponInfo = transaction.getMapCache(COUPON_INFO);
        couponInfo.put(couponId, couponCache);
    }

    private void setCouponCacheTTL(Long couponId, CouponCache couponCache) {
        RMapCache<Long, CouponCache> couponInfo = redissonClient.getMapCache(
            COUPON_INFO);
        couponInfo.expire(Duration.ofHours(couponExpirationHours));
    }

    private void processCouponIssuance(
        RTransaction transaction,
        RMap<Long, Set<Long>> couponIssuer
    ) {
        List<IssuedCoupon> issuedCoupons = new ArrayList<>();
        Set<Long> issuedCouponIds = couponIssuer.keySet();

        // 쿠폰 ID별 IssuedCoupon 생성 및 저장
        for (Long issuedCouponId : issuedCouponIds) {
            Set<Long> issuers = couponIssuer.get(issuedCouponId);
            Coupon coupon = getCoupon(issuedCouponId);
            issuedCoupons.addAll(createIssuedCoupon(issuers, coupon));

            coupon.decreaseQuantity(issuers.size());
            log.info("{} 쿠폰의 수량을 {}개 차감합니다.", issuedCouponId, issuers.size());

            updateCouponInfoWithIssuer(transaction, issuedCouponId, issuers);
            // 발급 쿠폰 캐시 삭제
            couponIssuer.remove(issuedCouponId);
        }
        issuedCouponRepository.saveAll(issuedCoupons);
        log.info("{}개의 쿠폰 발급을 완료했습니다.", issuedCoupons.size());
    }

    private List<IssuedCoupon> createIssuedCoupon(Set<Long> issuers, Coupon coupon) {
        return issuers.stream()
            .map(issuer -> IssuedCoupon.of(issuer, coupon))
            .toList();
    }

    private void updateCouponInfoWithIssuer(RTransaction transaction, Long couponId,
        Set<Long> issuers) {
        CouponCache couponCache = getCouponCache(transaction, couponId);
        couponCache.addIssuers(issuers);
        updateCouponCache(transaction, couponId, couponCache);
    }

    private Coupon getCoupon(Long couponId) {
        return couponRepository.findById(couponId).orElseThrow(
            () -> new CouponException(CouponErrorCode.COUPON_NOT_FOUND, couponId)
        );
    }

}
