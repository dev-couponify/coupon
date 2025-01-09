package com.couponify.couponapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.couponify")
@EnableScheduling
public class CouponApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(CouponApiApplication.class, args);
  }

}
