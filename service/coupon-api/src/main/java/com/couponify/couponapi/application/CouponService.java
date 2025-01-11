package com.couponify.couponapi.application;

import com.couponify.couponapi.exception.CouponErrorCode;
import com.couponify.couponapi.exception.CouponException;
import com.couponify.couponapi.presentation.request.CouponCreateRequest;
import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import com.couponify.coupondomain.domain.issuedCoupon.IssuedCoupon;
import com.couponify.coupondomain.domain.issuedCoupon.repository.IssuedCouponRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "CouponService")
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponService {

    private static final int QUANTITY_TO_ISSUE_COUPON = 1;
    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    @Transactional
    public Long create(CouponCreateRequest couponCreateRequest) {
        final Coupon coupon = CouponCreateRequest.toDomain(couponCreateRequest);
        final Coupon savedCoupon = couponRepository.save(coupon);
        return savedCoupon.getId();
    }

    @Transactional
    public Long issue(Long couponId, Long userId) {
        final Coupon coupon = validateCoupon(couponId);
        coupon.issue(QUANTITY_TO_ISSUE_COUPON);

        //TODO User 검증 필요

        final IssuedCoupon issuedCoupon = IssuedCoupon.of(userId, coupon);
        final IssuedCoupon savedIssuedCoupon = issuedCouponRepository.save(issuedCoupon);

        return savedIssuedCoupon.getId();
    }

    @Transactional
    public void expire() {
        List<Coupon> expiredCoupons = couponRepository.findExpiredCoupons(LocalDateTime.now());
        if (expiredCoupons.isEmpty()) {
            return;
        }
        expiredCoupons.forEach(Coupon::expire);
        log.info("Expired {} coupons", expiredCoupons.size());
    }

    private Coupon validateCoupon(Long couponId) {
        return couponRepository.findById(couponId).orElseThrow(
            () -> new CouponException(CouponErrorCode.COUPON_NOT_FOUND, couponId)
        );
    }

}
