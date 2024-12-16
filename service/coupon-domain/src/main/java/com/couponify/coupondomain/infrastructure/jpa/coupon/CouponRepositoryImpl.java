package com.couponify.coupondomain.infrastructure.jpa.coupon;

import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

  private final JpaCouponRepository jpaCouponRepository;

  @Override
  public Coupon save(Coupon coupon) {
    return jpaCouponRepository.save(coupon);
  }

  @Override
  public Optional<Coupon> findById(Long couponId) {
    return jpaCouponRepository.findById(couponId);
  }

  @Override
  public Optional<Coupon> findByIdForUpdate(Long couponId) {
    return jpaCouponRepository.findByIdForUpdate(couponId);
  }

  @Override
  public List<Coupon> findExpiredCoupons(LocalDateTime now) {
    return jpaCouponRepository.findAllByIssueEndAtBefore(now);
  }

  @Override
  public void flush() {
    jpaCouponRepository.flush();
  }

}
