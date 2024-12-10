package com.couponify.coupondomain.infrastructure.jpa.coupon;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCouponRepository extends JpaRepository<CouponEntity, Long> {

}
