package com.couponify.couponapi.application;

import com.couponify.couponapi.application.dto.CouponCreateDto;
import com.couponify.couponapi.application.dto.CouponServiceMapper;
import com.couponify.coupondomain.domain.Coupon;
import com.couponify.coupondomain.domain.repository.CouponRepository;
import com.couponify.coupondomain.infrastructure.jpa.CouponEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

  private final CouponRepository couponRepository;

  public Long create(CouponCreateDto couponCreateDto) {
    final Coupon coupon = CouponServiceMapper.toEntity(couponCreateDto);
    final CouponEntity couponEntity = CouponServiceMapper.toJpaEntity(coupon);
    final CouponEntity savedCoupon = couponRepository.save(couponEntity);
    return savedCoupon.getId();
  }

}
