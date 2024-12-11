package com.couponify.coupondomain.infrastructure.jpa.issuedCoupon;

import com.couponify.coupondomain.domain.issuedCoupon.IssuedCoupon;
import com.couponify.coupondomain.domain.issuedCoupon.repository.IssuedCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IssuedCouponRepositoryImpl implements IssuedCouponRepository {

  private final JpaIssuedCouponRepository jpaIssuedCouponRepository;

  @Override
  public IssuedCoupon save(IssuedCoupon issuedCoupon) {
    return jpaIssuedCouponRepository.save(issuedCoupon);
  }

}
