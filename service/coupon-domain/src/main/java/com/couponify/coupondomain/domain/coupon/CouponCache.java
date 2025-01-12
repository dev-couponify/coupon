package com.couponify.coupondomain.domain.coupon;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponCache {

    private Long id;
    private CouponStatus status;
    private Quantity quantity;
    private LocalDateTime issueStartAt;
    private LocalDateTime issueEndAt;

    public static CouponCache of(Coupon coupon) {
        return new CouponCache(
            coupon.getId(),
            coupon.getStatus(),
            new Quantity(coupon.getQuantity()),
            coupon.getIssueStartAt(),
            coupon.getIssueEndAt()
        );
    }

    public void issue(int quantity) {
        validateIssuable(quantity);
        decreaseQuantity(quantity);
    }

    public int getQuantity() {
        return quantity.getValue();
    }

    private void validateIssuable(int quantity) {
        validateIssueStatus();
        validateIssueQuantity(quantity);
        validateIssuePeriod();
    }

    private void validateIssueStatus() {
        if (!this.status.isIssuable()) {
            throw new IllegalArgumentException("쿠폰 발급 가능 상태가 아닙니다.");
        }
    }

    private void validateIssueQuantity(int quantity) {
        if (!this.quantity.isGreaterThanOrEqualTo(quantity)) {
            throw new IllegalArgumentException("쿠폰 수량이 부족합니다.");
        }
    }

    private void validateIssuePeriod() {
        if (!isIssuePeriod()) {
            throw new IllegalArgumentException("쿠폰 발급 기간이 아닙니다.");
        }
    }

    private boolean isIssuePeriod() {
        return (this.issueStartAt.isBefore(LocalDateTime.now())
            && this.issueEndAt.isAfter(LocalDateTime.now()));
    }

    private void decreaseQuantity(int quantity) {
        this.quantity.decrease(quantity);
        if (this.quantity.checkIsZero()) {
            soldOut();
        }
    }

    private void soldOut() {
        this.status = CouponStatus.SOLD_OUT;
    }

}
