package com.couponify.coupondomain.infrastructure.jpa.issuedCoupon;

import com.couponify.coupondomain.domain.issuedCoupon.IssuedCoupon;
import com.couponify.coupondomain.domain.issuedCoupon.repository.IssuedCouponRepository;
import com.couponify.coupondomain.util.mapper.CouponDomainMapper;
import com.couponify.coupondomain.util.mapper.IssuedCouponDomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaIssuedCouponRepositoryAdaptor implements IssuedCouponRepository {

  private final JpaIssuedCouponRepository jpaIssuedCouponRepository;

  @Override
  public IssuedCoupon save(IssuedCoupon issuedCoupon) {
    IssuedCouponEntity issuedCouponEntity = IssuedCouponEntity.create(
        issuedCoupon.getUserId(),
        CouponDomainMapper.toEntity(issuedCoupon.getCoupon()),
        issuedCoupon.isUsed()
    );
    IssuedCouponEntity savedIssuedCouponEntity = jpaIssuedCouponRepository.save(issuedCouponEntity);
    return IssuedCouponDomainMapper.toDomain(savedIssuedCouponEntity);
  }

}
