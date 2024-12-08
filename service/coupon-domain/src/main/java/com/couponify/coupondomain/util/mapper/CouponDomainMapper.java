package com.couponify.coupondomain.util.mapper;

import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.CouponStatus;
import com.couponify.coupondomain.domain.coupon.Quantity;
import com.couponify.coupondomain.infrastructure.jpa.coupon.CouponEntity;

public class CouponDomainMapper {

  public static Coupon toDomain(CouponEntity couponEntity) {
    return new Coupon(
        couponEntity.getId(),
        couponEntity.getName(),
        CouponStatus.valueOf(couponEntity.getStatus()),
        new Quantity(couponEntity.getQuantity())
    );
  }

}
