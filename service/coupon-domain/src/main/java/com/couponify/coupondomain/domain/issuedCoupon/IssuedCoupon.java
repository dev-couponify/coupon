package com.couponify.coupondomain.domain.issuedCoupon;

import com.couponify.coupondomain.domain.coupon.Coupon;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class IssuedCoupon {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "issued_coupon_id")
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coupon_id")
  private Coupon coupon;

  @Column(nullable = false)
  private Boolean isUsed;

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

  public static IssuedCoupon of(Long userId, Coupon coupon) {
    return new IssuedCoupon(userId, coupon);
  }

}
