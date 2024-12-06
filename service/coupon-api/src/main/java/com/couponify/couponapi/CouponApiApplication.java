package com.couponify.couponapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.couponify")
public class CouponApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(CouponApiApplication.class, args);
  }

}
