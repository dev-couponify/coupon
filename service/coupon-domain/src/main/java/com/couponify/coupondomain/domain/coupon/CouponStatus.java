package com.couponify.coupondomain.domain.coupon;

import lombok.Getter;

@Getter
public enum CouponStatus {

  AVAILABLE("발급 가능"), SOLD_OUT("수량 소진"), EXPIRED("발급 만료");

  private final String description;

  CouponStatus(String description) {
    this.description = description;
  }

  public boolean isIssuable() {
    return this.equals(AVAILABLE);
  }
}
