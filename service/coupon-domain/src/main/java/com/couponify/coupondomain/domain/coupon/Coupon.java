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

  private Coupon(String name, CouponStatus status, Quantity quantity) {
    this.name = name;
    this.status = status;
    this.quantity = quantity;
  }

  private Coupon(Long id, String name, CouponStatus status, Quantity quantity) {
    this.id = id;
    this.name = name;
    this.status = status;
    this.quantity = quantity;
  }

  public static Coupon of(String name, CouponStatus status, int quantity) {
    return new Coupon(name, status, new Quantity(quantity));
  }

  public void issue(int quantity) {
    checkIssuable(quantity);
    decreaseQuantity(quantity);
  }

  public void updateStatus(CouponStatus newStatus) {
    if (this.status != newStatus) {
      this.status = newStatus;
    }
  }

  private void checkIssuable(int quantity) {
    if (!this.status.isIssuable() || !isIssuable(quantity)) {
      throw new IllegalArgumentException("쿠폰 발급이 불가합니다. 상태 또는 수량이 발급 조건을 충족하지 않습니다.");
    }
  }

  private boolean isIssuable(int quantity) {
    return this.quantity.isSufficientFor(quantity);
  }

  private void decreaseQuantity(int quantity) {
    this.quantity.decrease(quantity);
    if (this.quantity.isNoQuantity()) {
      updateStatus(CouponStatus.SOLD_OUT);
    }
  }

}
