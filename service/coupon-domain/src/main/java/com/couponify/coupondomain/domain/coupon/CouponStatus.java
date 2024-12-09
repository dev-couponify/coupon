package com.couponify.coupondomain.domain.coupon;

import lombok.Getter;

@Getter
public enum CouponStatus {

  ISSUABLE("발급 가능"), NOT_ISSUABLE("발급 불가");

  private final String description;

  CouponStatus(String description) {
    this.description = description;
  }

  public boolean isIssuable() {
    return this.equals(ISSUABLE);
  }
}
