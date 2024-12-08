package com.couponify.coupondomain.domain.issuedCoupon;

import lombok.Getter;

@Getter
public class IssuedCoupon {

  private Long id;
  private Long userId;
  private Long couponId;
  private boolean isUsed;

  private IssuedCoupon() {

  }

  private IssuedCoupon(Long userId, Long couponId) {
    this.userId = userId;
    this.couponId = couponId;
    this.isUsed = false;
  }

  private IssuedCoupon(Long id, Long userId, Long couponId, boolean isUsed) {
    this.id = id;
    this.userId = userId;
    this.couponId = couponId;
    this.isUsed = isUsed;
  }

  public static IssuedCoupon create(Long userId, Long couponId) {
    return new IssuedCoupon(userId, couponId);
  }

}
