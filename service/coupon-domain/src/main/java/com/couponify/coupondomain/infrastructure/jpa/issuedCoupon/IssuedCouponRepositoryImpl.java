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
    IssuedCouponEntity issuedCouponEntity = IssuedCouponEntity.create(
        issuedCoupon.getUserId(),
        issuedCoupon.getCoupon(),
        issuedCoupon.isUsed()
    );
    IssuedCouponEntity savedIssuedCouponEntity = jpaIssuedCouponRepository.save(issuedCouponEntity);
    return IssuedCoupon.formEntity(savedIssuedCouponEntity);
  }

}
