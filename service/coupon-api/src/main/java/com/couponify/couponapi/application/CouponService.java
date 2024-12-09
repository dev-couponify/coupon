package com.couponify.couponapi.application;

import com.couponify.couponapi.application.dto.CouponCreateDto;
import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import com.couponify.coupondomain.domain.issuedCoupon.IssuedCoupon;
import com.couponify.coupondomain.domain.issuedCoupon.repository.IssuedCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponService {

  private final CouponRepository couponRepository;
  private final IssuedCouponRepository issuedCouponRepository;

  private static final int COUPON_ISSUE_QUANTITY = 1;

  @Transactional
  public Long create(CouponCreateDto couponCreateDto) {
    final Coupon coupon = Coupon.create(
        couponCreateDto.getName(),
        couponCreateDto.getStatus(),
        couponCreateDto.getQuantity()
    );
    final Coupon savedCoupon = couponRepository.save(coupon);
    return savedCoupon.getId();
  }

  @Transactional
  public Long issue(Long couponId, Long userId) {
    final Coupon coupon = validateCoupon(couponId);
    coupon.issue(COUPON_ISSUE_QUANTITY);
    couponRepository.save(coupon);

    //TODO User 검증 필요

    final IssuedCoupon issuedCoupon = IssuedCoupon.create(userId, coupon);
    final IssuedCoupon savedIssuedCoupon = issuedCouponRepository.save(issuedCoupon);

    return savedIssuedCoupon.getId();
  }

  public Coupon validateCoupon(Long couponId) {
    return couponRepository.findById(couponId).orElseThrow(
        () -> new IllegalArgumentException("존재하지 않는 쿠폰입니다.") // TODO 커스텀 예외 처리
    );
  }

}
