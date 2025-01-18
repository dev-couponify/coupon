package com.couponify.coupondomain.infrastructure.jpa.issuedCoupon;

import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.issuedCoupon.IssuedCoupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaIssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {

    @Query("SELECT i.userId from IssuedCoupon i WHERE i.coupon = :coupon")
    List<Long> findUserIdsByCoupon(@Param("coupon") Coupon coupon);

}
