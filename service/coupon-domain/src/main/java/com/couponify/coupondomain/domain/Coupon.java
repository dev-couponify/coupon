package com.couponify.coupondomain.domain;

import lombok.Getter;

@Getter
public class Coupon {

  private Long id;
  private String name;
  private CouponStatus status;
  private Quantity quantity;

  public Coupon() {
  }

  private Coupon(String name, CouponStatus status, Quantity quantity) {
    this.name = name;
    this.status = status;
    this.quantity = quantity;
  }

  public Coupon(Long id, String name, CouponStatus status, Quantity quantity) {
    this.id = id;
    this.name = name;
    this.status = status;
    this.quantity = quantity;
  }

  public static Coupon create(String name, CouponStatus status, int quantity) {
    return new Coupon(name, status, new Quantity(quantity));
  }

}
