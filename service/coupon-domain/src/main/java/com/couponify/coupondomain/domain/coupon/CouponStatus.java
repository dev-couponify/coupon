package com.couponify.coupondomain.domain.coupon;

import lombok.Getter;

@Getter
public enum CouponStatus {

  AVAILABLE("발급 가능"), UNAVAILABLE("발급 불가");

  private final String description;

  CouponStatus(String description) {
    this.description = description;
  }
}
