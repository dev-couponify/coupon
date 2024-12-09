package com.couponify.coupondomain.util.mapper;

import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.CouponStatus;
import com.couponify.coupondomain.infrastructure.jpa.coupon.CouponEntity;

public class CouponDomainMapper {

  public static Coupon toDomain(CouponEntity couponEntity) {
    return new Coupon(
        couponEntity.getId(),
        couponEntity.getName(),
        CouponStatus.valueOf(couponEntity.getStatus()),
        couponEntity.getQuantity()
    );
  }

  public static CouponEntity toEntity(Coupon coupon) {
    return new CouponEntity(
        coupon.getId(),
        coupon.getName(),
        coupon.getStatus().name(),
        coupon.getQuantity().getQuantity()
    );
  }


}
