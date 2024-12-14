package com.couponify.coupondomain.domain.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "COUPON")
public class Coupon {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "coupon_id")
  private Long id;

  @Column(length = 100, nullable = false)
  private String name;

  @Column(length = 10, nullable = false)
  @Enumerated(value = EnumType.STRING)
  private CouponStatus status;

  @Column(nullable = false)
  @Embedded
  private Quantity quantity;

  @Column(nullable = false)
  private LocalDateTime issueStartAt;

  @Column(nullable = false)
  private LocalDateTime issueEndAt;

  private Coupon(String name, CouponStatus status, Quantity quantity, LocalDateTime issueStartAt,
      LocalDateTime issueEndAt) {
    validateSetPeriod(issueStartAt, issueEndAt);
    this.name = name;
    this.status = status;
    this.quantity = quantity;
    this.issueStartAt = issueStartAt;
    this.issueEndAt = issueEndAt;
  }

  public static Coupon of(String name, CouponStatus status, int quantity,
      LocalDateTime issueStartAt, LocalDateTime issueEndAt) {
    return new Coupon(name, status, new Quantity(quantity), issueStartAt, issueEndAt);
  }

  public void issue(int quantity) {
    validateIssuable(quantity);
    decreaseQuantity(quantity);
  }

  public void expire() {
    updateStatus(CouponStatus.EXPIRED);
  }

  private void validateSetPeriod(LocalDateTime issueStartAt, LocalDateTime issueEndAt) {
    if (!isValidPeriod(issueStartAt, issueEndAt)) {
      throw new IllegalArgumentException("쿠폰 생성시 발급 시작 일시와 종료 일시는 미래여야 합니다.");
    }
  }

  private boolean isValidPeriod(LocalDateTime issueStartAt, LocalDateTime issueEndAt) {
    return (issueStartAt.isAfter(LocalDateTime.now()) &&
        issueEndAt.isAfter(LocalDateTime.now()) &&
        !issueStartAt.isEqual(issueEndAt));
  }

  private void updateStatus(CouponStatus newStatus) {
    if (this.status != newStatus) {
      this.status = newStatus;
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
    if (this.quantity.isZero()) {
      updateStatus(CouponStatus.SOLD_OUT);
    }
  }

}
