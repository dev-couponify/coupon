package com.couponify.coupondomain.infrastructure.jpa.issuedCoupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
  private Long usedId;

  @Column(nullable = false)
  private Long couponId;

  @Column(nullable = false)
  private Boolean isUsed;

  private IssuedCouponEntity(Long usedId, Long couponId, Boolean isUsed) {
    this.usedId = usedId;
    this.couponId = couponId;
    this.isUsed = isUsed;
  }

  public static IssuedCouponEntity create(Long usedId, Long couponId, Boolean isUsed) {
    return new IssuedCouponEntity(usedId, couponId, isUsed);
  }

}
