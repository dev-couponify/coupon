package com.couponify.coupondomain.domain;

import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import com.couponify.coupondomain.infrastructure.jpa.coupon.CouponRepositoryImpl;
import com.couponify.coupondomain.infrastructure.jpa.coupon.JpaCouponRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

  @Bean
  public CouponRepository couponRepository(JpaCouponRepository jpaCouponRepository) {
    return new CouponRepositoryImpl(jpaCouponRepository);
  }

}
