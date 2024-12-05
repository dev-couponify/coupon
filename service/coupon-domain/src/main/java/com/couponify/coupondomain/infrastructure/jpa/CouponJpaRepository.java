package com.couponify.coupondomain.infrastructure.jpa;

import com.couponify.coupondomain.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

}
