package com.couponify.coupondomain.domain.issuedCoupon;

import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.infrastructure.jpa.issuedCoupon.IssuedCouponEntity;
import lombok.Getter;

@Getter
public class IssuedCoupon {

  private Long id;
  private Long userId;
  private Coupon coupon;
  private boolean isUsed;

  private IssuedCoupon() {

  }

  private IssuedCoupon(Long userId, Coupon coupon) {
    this.userId = userId;
    this.coupon = coupon;
    this.isUsed = false;
  }

  private IssuedCoupon(Long id, Long userId, Coupon coupon, boolean isUsed) {
    this.id = id;
    this.userId = userId;
    this.coupon = coupon;
    this.isUsed = isUsed;
  }


  public static IssuedCoupon create(Long userId, Coupon coupon) {
    return new IssuedCoupon(userId, coupon);
  }

  public static IssuedCoupon formEntity(IssuedCouponEntity issuedCouponEntity) {
    return new IssuedCoupon(
        issuedCouponEntity.getId(),
        issuedCouponEntity.getUserId(),
        Coupon.fromEntity(issuedCouponEntity.getCoupon()),
        issuedCouponEntity.getIsUsed()
    );
  }

}
