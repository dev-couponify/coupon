package com.couponify.coupondomain.infrastructure.jpa.coupon;

import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import com.couponify.coupondomain.util.mapper.CouponDomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaCouponRepositoryAdaptor implements CouponRepository {

  private final JpaCouponRepository jpaCouponRepository;

  @Override
  public Coupon save(Coupon coupon) {
    CouponEntity couponEntity = CouponEntity.create(
        coupon.getName(),
        coupon.getStatus().name(),
        coupon.getQuantity().getQuantity()
    );
    CouponEntity savedCouponEntity = jpaCouponRepository.save(couponEntity);
    return CouponDomainMapper.toDomain(savedCouponEntity);
  }

}
