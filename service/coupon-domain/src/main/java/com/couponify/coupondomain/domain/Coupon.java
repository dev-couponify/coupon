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

  public Coupon(String name, CouponStatus status, int quantity) {
    this.name = name;
    this.status = status;
    this.quantity = new Quantity(quantity);
  }

}
