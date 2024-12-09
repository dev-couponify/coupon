package com.couponify.coupondomain.util.mapper;

import com.couponify.coupondomain.domain.Coupon;
import com.couponify.coupondomain.domain.CouponStatus;
import com.couponify.coupondomain.domain.Quantity;
import com.couponify.coupondomain.infrastructure.jpa.CouponEntity;

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
