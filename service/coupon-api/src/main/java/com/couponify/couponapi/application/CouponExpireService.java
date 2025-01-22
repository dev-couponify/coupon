package com.couponify.couponapi.application;

import com.couponify.couponapi.common.CouponPrefix;
import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.CouponCache;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "CouponExpireService")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponExpireService {

    private final CouponRepository couponRepository;
    private final RedissonClient redissonClient;

    @Transactional
    public void expire() {
        List<Coupon> expiredCoupons = couponRepository.findExpiredCoupons(LocalDateTime.now());
        if (expiredCoupons.isEmpty()) {
            return;
        }
        expiredCoupons.forEach(Coupon::expire);
        deleteCouponCache(expiredCoupons.stream().map(Coupon::getId).toList());
        log.info("Expired {} coupons", expiredCoupons.size());
    }

    private void deleteCouponCache(List<Long> couponIds) {
        couponIds.forEach(couponId -> deleteCouponCache(couponId));
    }

    private void deleteCouponCache(Long couponId) {
        RMap<Long, CouponCache> couponInfo = redissonClient.getMap(CouponPrefix.COUPON_INFO);
        couponInfo.remove(couponId);
    }

}
