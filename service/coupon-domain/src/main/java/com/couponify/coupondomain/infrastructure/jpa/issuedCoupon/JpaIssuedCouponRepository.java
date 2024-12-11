package com.couponify.coupondomain.infrastructure.jpa.issuedCoupon;

import com.couponify.coupondomain.domain.issuedCoupon.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaIssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {

}
