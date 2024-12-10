package com.couponify.couponapi.application;

import com.couponify.couponapi.exception.CouponErrorCode;
import com.couponify.couponapi.exception.CouponException;
import com.couponify.couponapi.presentation.request.CouponCreateRequest;
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
  public Long create(CouponCreateRequest couponCreateRequest) {
    final Coupon coupon = CouponCreateRequest.toDomain(couponCreateRequest);
    final Coupon savedCoupon = couponRepository.save(coupon);
    return savedCoupon.getId();
  }

  @Transactional
  public Long issue(Long couponId, Long userId) {
    final Coupon coupon = validateCoupon(couponId);
    coupon.issue(COUPON_ISSUE_QUANTITY);
    couponRepository.save(coupon);

    //TODO User 검증 필요

    final IssuedCoupon issuedCoupon = IssuedCoupon.of(userId, coupon);
    final IssuedCoupon savedIssuedCoupon = issuedCouponRepository.save(issuedCoupon);

    return savedIssuedCoupon.getId();
  }

  private Coupon validateCoupon(Long couponId) {
    return couponRepository.findById(couponId).orElseThrow(
        () -> new CouponException(CouponErrorCode.COUPON_NOT_FOUND, couponId)
    );
  }

}
