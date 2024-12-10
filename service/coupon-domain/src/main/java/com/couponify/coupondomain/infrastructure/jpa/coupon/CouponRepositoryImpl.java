package com.couponify.coupondomain.infrastructure.jpa.coupon;

import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

  private final JpaCouponRepository jpaCouponRepository;

  @Override
  public Coupon save(Coupon coupon) {
    CouponEntity couponEntity;
    if (isNewCoupon(coupon)) {
      couponEntity = createCouponEntity(coupon);
    } else {
      couponEntity = updateCouponEntity(coupon);
    }
    CouponEntity savedCouponEntity = jpaCouponRepository.save(couponEntity);
    return Coupon.fromEntity(savedCouponEntity);
  }

  @Override
  public Optional<Coupon> findById(Long couponId) {
    return jpaCouponRepository.findById(couponId)
        .map(Coupon::fromEntity);
  }

  private CouponEntity createCouponEntity(Coupon coupon) {
    return CouponEntity.create(
        coupon.getName(),
        coupon.getStatus().name(),
        coupon.getQuantity().getQuantity()
    );
  }

  private CouponEntity updateCouponEntity(Coupon coupon) {
    return CouponEntity.fromDomain(coupon);
  }

  private boolean isNewCoupon(Coupon coupon) {
    return coupon.getId() == null;
  }

}
