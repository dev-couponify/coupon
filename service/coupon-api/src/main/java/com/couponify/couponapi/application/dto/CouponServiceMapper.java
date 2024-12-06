package com.couponify.couponapi.application.dto;

import com.couponify.coupondomain.domain.Coupon;
import com.couponify.coupondomain.infrastructure.jpa.CouponEntity;

public class CouponServiceMapper {

  public static Coupon toEntity(CouponCreateDto couponCreateDto) {
    return new Coupon(
        couponCreateDto.getName(),
        couponCreateDto.getStatus(),
        couponCreateDto.getQuantity()
    );
  }

  public static CouponEntity toJpaEntity(Coupon coupon) {
    return new CouponEntity(
        coupon.getName(),
        coupon.getStatus().name(),
        coupon.getQuantity().getQuantity()
    );
  }

}
