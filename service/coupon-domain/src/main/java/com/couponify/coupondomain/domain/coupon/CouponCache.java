package com.couponify.coupondomain.domain.coupon;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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
    private Set<Long> issuerIds = new HashSet<>();

    public static CouponCache of(Coupon coupon, Set<Long> issuerIds) {
        return new CouponCache(
            coupon.getId(),
            coupon.getStatus(),
            new Quantity(coupon.getQuantity()),
            coupon.getIssueStartAt(),
            coupon.getIssueEndAt(),
            issuerIds
        );
    }

    public void issue(Long userId, int quantity) {
        validateDuplicateIssuer(userId);
        validateIssuable(quantity);
        decreaseQuantity(quantity);
    }

    public void addIssuers(Set<Long> issuerIds) {
        issuerIds.forEach(this::addIssuer);
    }

    private void addIssuer(Long userId) {
        issuerIds.add(userId);
    }

    private void validateDuplicateIssuer(Long userId) {
        if (issuerIds.contains(userId)) {
            throw new IllegalArgumentException("이미 쿠폰을 발급한 사용자입니다.");
        }
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
