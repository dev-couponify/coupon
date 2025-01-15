package com.couponify.couponapi.application;


import com.couponify.couponapi.common.CouponPrefix;
import com.couponify.couponapi.exception.CouponErrorCode;
import com.couponify.couponapi.exception.CouponException;
import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.CouponCache;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RSet;
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
    private final RedissonClient redissonClient;

    @Value("${cache.coupon.expiration.hours}")
    private Long couponExpirationHours;

    public void issue(Long couponId, Long userId) {
        RTransaction transaction = redissonClient.createTransaction(TransactionOptions.defaults());
        try {
            CouponCache couponCache = getCouponCache(transaction, couponId);

            checkUserAlreadyIssued(transaction, couponId, userId);
            couponCache.issue(QUANTITY_TO_ISSUE_COUPON);
            addIssuer(transaction, couponId, userId);
            updateCouponCache(transaction, couponId, couponCache);

            transaction.commit();

            setCouponCacheTTL(couponId, couponCache);
        } catch (Exception e) {
            transaction.rollback();
            throw new CouponException(CouponErrorCode.TRANSACTION_COMMIT_FAILED, e.getMessage());
        }
    }

    private CouponCache getCouponCache(RTransaction transaction, Long couponId) {
        RMapCache<Long, CouponCache> couponInfo = transaction.getMapCache(CouponPrefix.COUPON_INFO);
        CouponCache couponCache = couponInfo.get(couponId);

        if (couponCache == null) {
            Coupon coupon = getCoupon(couponId);
            return CouponCache.of(coupon);
        }

        return couponCache;
    }

    private void checkUserAlreadyIssued(RTransaction transaction, Long couponId, Long userId) {
        RSet<Long> issuedUsers = transaction.getSet(CouponPrefix.COUPON_ISSUER + couponId);
        if (issuedUsers.contains(userId)) {
            throw new CouponException(CouponErrorCode.COUPON_ALREADY_ISSUED);
        }
    }

    private void addIssuer(RTransaction transaction, Long couponId, Long userId) {
        RSet<Long> issuedUsers = transaction.getSet(CouponPrefix.COUPON_ISSUER + couponId);
        issuedUsers.add(userId);
    }

    private void updateCouponCache(RTransaction transaction, Long couponId,
        CouponCache couponCache) {
        RMapCache<Long, CouponCache> couponInfo = transaction.getMapCache(CouponPrefix.COUPON_INFO);
        couponInfo.put(couponId, couponCache);
    }

    private void setCouponCacheTTL(Long couponId, CouponCache couponCache) {
        RMapCache<Long, CouponCache> couponInfo = redissonClient.getMapCache(
            CouponPrefix.COUPON_INFO);
        couponInfo.expire(Duration.ofHours(couponExpirationHours));
    }

    private Coupon getCoupon(Long couponId) {
        return couponRepository.findById(couponId).orElseThrow(
            () -> new CouponException(CouponErrorCode.COUPON_NOT_FOUND, couponId)
        );
    }

}
