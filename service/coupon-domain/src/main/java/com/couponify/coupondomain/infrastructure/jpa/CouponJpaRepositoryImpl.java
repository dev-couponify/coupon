package com.couponify.coupondomain.infrastructure.jpa;

import com.couponify.coupondomain.infrastructure.CouponRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CouponJpaRepositoryImpl implements CouponRepository {

  private final CouponJpaRepository couponJpaRepository;

  @Override
  public CouponEntity save(CouponEntity coupon) {
    return couponJpaRepository.save(coupon);
  }

}
