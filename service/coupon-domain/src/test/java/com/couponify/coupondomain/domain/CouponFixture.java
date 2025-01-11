package com.couponify.coupondomain.domain;

import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.CouponStatus;
import java.time.LocalDateTime;

public class CouponFixture {

    public static Coupon createCoupon() {
        final String name = "샘플 쿠폰";
        final CouponStatus status = CouponStatus.AVAILABLE;
        final int quantity = 100;
        final LocalDateTime issueStartAt = LocalDateTime.now().plusDays(1);
        final LocalDateTime issueEndAt = LocalDateTime.now().plusDays(2);
        return Coupon.of(name, status, quantity, issueStartAt, issueEndAt);
    }

}
