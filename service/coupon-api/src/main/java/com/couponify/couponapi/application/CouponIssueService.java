package com.couponify.couponapi.application;


import com.couponify.couponapi.common.CouponPrefix;
import com.couponify.couponapi.exception.CouponErrorCode;
import com.couponify.couponapi.exception.CouponException;
import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.CouponCache;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import com.couponify.coupondomain.domain.issuedCoupon.repository.IssuedCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
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

    @Transactional
    public void issue(Long couponId, Long userId) {
        CouponCache couponCache = getCouponCache(couponId);
        checkUserAlreadyIssued(couponId, userId);

        // TODO User 검증 필요

        couponCache.issue(QUANTITY_TO_ISSUE_COUPON);

        addIssuer(couponId, userId);
        updateCouponCache(couponId, couponCache);
    }

    private CouponCache getCouponCache(Long couponId) {
        RMap<Long, CouponCache> couponInfo = redissonClient.getMap(CouponPrefix.COUPON_INFO);
        CouponCache couponCache = couponInfo.get(couponId);

        if (couponCache == null) {
            Coupon coupon = getCoupon(couponId);
            couponCache = CouponCache.of(coupon);
            couponInfo.put(couponId, couponCache);
        }

        return couponCache;
    }

    private Coupon getCoupon(Long couponId) {
        return couponRepository.findById(couponId).orElseThrow(
            () -> new CouponException(CouponErrorCode.COUPON_NOT_FOUND, couponId)
        );
    }

    private void checkUserAlreadyIssued(Long couponId, Long userId) {
        RSet<Long> issuedUsers = redissonClient.getSet(CouponPrefix.COUPON_ISSUER + couponId);
        if (issuedUsers.contains(userId)) {
            throw new CouponException(CouponErrorCode.COUPON_ALREADY_ISSUED);
        }
    }

    private void addIssuer(Long couponId, Long userId) {
        RSet<Long> issuedUsers = redissonClient.getSet(CouponPrefix.COUPON_ISSUER + couponId);
        issuedUsers.add(userId);
    }

    private void updateCouponCache(Long couponId, CouponCache couponCache) {
        RMap<Long, CouponCache> couponInfo = redissonClient.getMap(CouponPrefix.COUPON_INFO);
        couponInfo.put(couponId, couponCache);
    }

}
