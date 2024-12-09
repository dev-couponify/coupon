package com.couponify.coupondomain.infrastructure.jpa.issuedCoupon;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaIssuedCouponRepository extends JpaRepository<IssuedCouponEntity, Long> {

}
