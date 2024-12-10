package com.couponify.coupondomain.infrastructure.jpa.issuedCoupon;

import com.couponify.coupondomain.infrastructure.jpa.coupon.CouponEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ISSUED_COUPON")
public class IssuedCouponEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "issued_coupon_id")
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @ManyToOne
  @JoinColumn(name = "coupon_id")
  private CouponEntity coupon;

  @Column(nullable = false)
  private Boolean isUsed;

  private IssuedCouponEntity(Long userId, CouponEntity coupon, Boolean isUsed) {
    this.userId = userId;
    this.coupon = coupon;
    this.isUsed = isUsed;
  }

  public static IssuedCouponEntity create(Long userId, CouponEntity coupon, Boolean isUsed) {
    return new IssuedCouponEntity(userId, coupon, isUsed);
  }

}
