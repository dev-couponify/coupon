package com.couponify.coupondomain.domain.coupon.repository;


import com.couponify.coupondomain.domain.coupon.Coupon;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponRepository {

    Coupon save(Coupon coupon);

    Optional<Coupon> findById(Long couponId);

    List<Coupon> findExpiredCoupons(LocalDateTime now);

    void flush();

}
