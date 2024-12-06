package com.couponify.coupondomain.domain.repository;

import com.couponify.coupondomain.infrastructure.jpa.CouponEntity;

public interface CouponRepository {

  public CouponEntity save(CouponEntity coupon);

}
