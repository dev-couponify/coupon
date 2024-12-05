package com.couponify.coupondomain.infrastructure.jpa;

import com.couponify.coupondomain.domain.CouponStatus;
import com.couponify.coupondomain.domain.Quantity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
  private Long id;

  @Column(length = 100, nullable = false)
  private String name;

  @Enumerated(value = EnumType.STRING)
  @Column(length = 10, nullable = false)
  private CouponStatus status;

  @Embedded
  @Column(nullable = false)
  private Quantity quantity;

}
