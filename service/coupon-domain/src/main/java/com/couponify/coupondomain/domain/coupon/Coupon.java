package com.couponify.coupondomain.domain.coupon;

import lombok.Getter;

@Getter
public class Coupon {

  private Long id;
  private String name;
  private CouponStatus status;
  private Quantity quantity;

  private Coupon() {
  }

  private Coupon(String name, CouponStatus status, Quantity quantity) {
    this.name = name;
    this.status = status;
    this.quantity = quantity;
  }

  public Coupon(Long id, String name, CouponStatus status, int quantity) {
    this.id = id;
    this.name = name;
    this.status = status;
    this.quantity = new Quantity(quantity);
  }

  public static Coupon create(String name, CouponStatus status, int quantity) {
    return new Coupon(name, status, new Quantity(quantity));
  }

  public void issue(int quantity) {
    checkIssuable();
    decreaseQuantity(quantity);
  }

  public void checkIssuable() {
    if (!this.status.isIssuable()) {
      throw new IllegalArgumentException("쿠폰 발급이 불가합니다.");
    }
  }

  public void decreaseQuantity(int quantity) {
    this.quantity.subtract(quantity);
  }

}
