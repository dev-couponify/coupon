package com.couponify.coupondomain.infrastructure;

import com.couponify.coupondomain.infrastructure.jpa.CouponEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository {

  public CouponEntity save(CouponEntity coupon);

}
