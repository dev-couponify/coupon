package com.couponify.coupondomain.infrastructure.jpa;

import com.couponify.coupondomain.domain.Coupon;
import com.couponify.coupondomain.infrastructure.CouponRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CouponJpaRepositoryImpl implements CouponRepository {

  private final CouponJpaRepository couponJpaRepository;

  @Override
  public Coupon save(Coupon coupon) {
    return couponJpaRepository.save(coupon);
  }

}
