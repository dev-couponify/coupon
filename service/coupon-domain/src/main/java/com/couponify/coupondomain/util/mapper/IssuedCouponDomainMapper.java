package com.couponify.coupondomain.util.mapper;

import com.couponify.coupondomain.domain.issuedCoupon.IssuedCoupon;
import com.couponify.coupondomain.infrastructure.jpa.issuedCoupon.IssuedCouponEntity;

public class IssuedCouponDomainMapper {

  public static IssuedCoupon toDomain(IssuedCouponEntity issuedCouponEntity) {
    return new IssuedCoupon(
        issuedCouponEntity.getId(),
        issuedCouponEntity.getUserId(),
        CouponDomainMapper.toDomain(issuedCouponEntity.getCoupon()),
        issuedCouponEntity.getIsUsed()
    );
  }

}
