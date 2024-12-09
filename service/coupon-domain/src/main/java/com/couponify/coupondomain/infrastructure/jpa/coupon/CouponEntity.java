package com.couponify.coupondomain.infrastructure.jpa.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "COUPON")
public class CouponEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "coupon_id")
  private Long id;

  @Column(length = 100, nullable = false)
  private String name;

  @Column(length = 10, nullable = false)
  private String status;

  @Column(nullable = false)
  private Integer quantity;

  private CouponEntity(String name, String status, Integer quantity) {
    this.name = name;
    this.status = status;
    this.quantity = quantity;
  }

  public CouponEntity(Long id, String name, String status, Integer quantity) {
    this.id = id;
    this.name = name;
    this.status = status;
    this.quantity = quantity;
  }

  public static CouponEntity create(String name, String status, Integer quantity) {
    return new CouponEntity(name, status, quantity);
  }

}
