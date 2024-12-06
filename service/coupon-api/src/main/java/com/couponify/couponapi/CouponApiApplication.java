package com.couponify.couponapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.couponify")
@EntityScan("com.couponify.coupondomain")
@EnableJpaRepositories("com.couponify.coupondomain")
public class CouponApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(CouponApiApplication.class, args);
  }

}
