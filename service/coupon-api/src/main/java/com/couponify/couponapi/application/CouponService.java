package com.couponify.couponapi.application;

import com.couponify.couponapi.application.dto.CouponCreateDto;
import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

  private final CouponRepository couponRepository;

  public Long create(CouponCreateDto couponCreateDto) {
    final Coupon coupon = Coupon.create(
        couponCreateDto.getName(),
        couponCreateDto.getStatus(),
        couponCreateDto.getQuantity()
    );
    final Coupon savedCoupon = couponRepository.save(coupon);
    return savedCoupon.getId();
  }

}
