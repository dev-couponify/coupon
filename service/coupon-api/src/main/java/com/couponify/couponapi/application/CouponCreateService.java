package com.couponify.couponapi.application;

import com.couponify.couponapi.presentation.request.CouponCreateRequest;
import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "CouponCreateService")
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponCreateService {

    private final CouponRepository couponRepository;

    @Transactional
    public Long create(CouponCreateRequest couponCreateRequest) {
        final Coupon coupon = CouponCreateRequest.toDomain(couponCreateRequest);
        final Coupon savedCoupon = couponRepository.save(coupon);
        return savedCoupon.getId();
    }

}
