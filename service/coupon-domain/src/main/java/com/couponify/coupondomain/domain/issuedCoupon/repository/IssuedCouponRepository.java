package com.couponify.coupondomain.domain.issuedCoupon.repository;


import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.issuedCoupon.IssuedCoupon;
import java.util.List;

public interface IssuedCouponRepository {

    IssuedCoupon save(IssuedCoupon issuedCoupon);

    List<IssuedCoupon> saveAll(List<IssuedCoupon> issuedCoupons);

    List<Long> findUserIdsByCoupon(Coupon coupon);

}
