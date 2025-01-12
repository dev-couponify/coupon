package com.couponify.couponapi.application;

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
    public Long issue(Long couponId, Long userId) {
        CouponCache couponCache = getCouponCache(couponId);
        checkUserAlreadyIssued(couponId, userId);

        couponCache.issue(QUANTITY_TO_ISSUE_COUPON);

        addIssuedUser(couponId, userId);
        updateCouponCache(couponId, couponCache);
        return 1L;
    }

    private CouponCache getCouponCache(Long couponId) {
        RMap<Long, CouponCache> couponInfo = redissonClient.getMap("coupon:info");
        CouponCache couponCache = couponInfo.get(couponId);

        if (couponCache == null) {
            Coupon coupon = validateCoupon(couponId);
            couponCache = CouponCache.of(coupon);
            couponInfo.put(couponId, couponCache);
        }

        return couponCache;
    }

    private Coupon validateCoupon(Long couponId) {
        return couponRepository.findById(couponId).orElseThrow(
            () -> new CouponException(CouponErrorCode.COUPON_NOT_FOUND, couponId)
        );
    }

    private void checkUserAlreadyIssued(Long couponId, Long userId) {
        RSet<Long> issuedUsers = redissonClient.getSet("coupon:issued:" + couponId);
        if (issuedUsers.contains(userId)) {
            throw new CouponException(CouponErrorCode.COUPON_ALREADY_ISSUED);
        }
    }

    private void addIssuedUser(Long couponId, Long userId) {
        RSet<Long> issuedUsers = redissonClient.getSet("coupon:issued:" + couponId);
        issuedUsers.add(userId);
    }

    private void updateCouponCache(Long couponId, CouponCache couponCache) {
        RMap<Long, CouponCache> couponInfo = redissonClient.getMap("coupon:info");
        couponInfo.put(couponId, couponCache);
    }
    
}
