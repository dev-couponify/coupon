package com.couponify.coupondomain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CouponDomainApplication {

  public static void main(String[] args) {
    SpringApplication.run(CouponDomainApplication.class, args);
  }

}
