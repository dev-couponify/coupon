package com.couponify.coupondomain.infrastructure;

import com.couponify.coupondomain.domain.Coupon;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository {

  public Coupon save(Coupon coupon);

}
