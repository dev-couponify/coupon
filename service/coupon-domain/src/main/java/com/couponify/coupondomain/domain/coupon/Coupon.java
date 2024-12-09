package com.couponify.coupondomain.domain.coupon;

import com.couponify.coupondomain.infrastructure.jpa.coupon.CouponEntity;
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

  private Coupon(Long id, String name, CouponStatus status, Quantity quantity) {
    this.id = id;
    this.name = name;
    this.status = status;
    this.quantity = quantity;
  }

  public static Coupon create(String name, CouponStatus status, int quantity) {
    return new Coupon(name, status, new Quantity(quantity));
  }

  public static Coupon fromEntity(CouponEntity couponEntity) {
    return new Coupon(couponEntity.getId(),
        couponEntity.getName(),
        CouponStatus.valueOf(couponEntity.getStatus()),
        new Quantity(couponEntity.getQuantity()));
  }

  public void issue(int quantity) {
    checkIssuable();
    decreaseQuantity(quantity);
  }

  private void checkIssuable() {
    if (!this.status.isIssuable()) {
      throw new IllegalArgumentException("쿠폰 발급이 불가합니다.");
    }
  }

  public void decreaseQuantity(int quantity) {
    this.quantity.subtract(quantity);
  }

}
