package com.couponify.coupondomain.infrastructure.jpa;

import com.couponify.coupondomain.domain.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaCouponRepositoryAdaptor implements CouponRepository {

  private final JpaCouponRepository jpaCouponRepository;

  @Override
  public CouponEntity save(CouponEntity coupon) {
    return jpaCouponRepository.save(coupon);
  }

}
