package com.couponify.couponapi.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.couponify.couponapi.CouponFixture;
import com.couponify.couponapi.application.CouponService;
import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import com.couponify.coupondomain.domain.issuedCoupon.repository.IssuedCouponRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CouponConcurrentTest {

  @Autowired
  private CouponService couponService;
  @Autowired
  private CouponRepository couponRepository;
  @Autowired
  private IssuedCouponRepository issuedCouponRepository;

  @Test
  @DisplayName("100명의 사용자가 동시에 쿠폰을 발급할 때, 쿠폰 수량이 정상적으로 감소하고 100개의 쿠폰이 발급된다.")
  void issueCouponWith100Users() throws InterruptedException {
    // given
    final int threadCount = 100;
    final Long couponId = couponRepository.save(CouponFixture.createCoupon()).getId();
    final Long userId = 1L;
    final int expectedCouponQuantity = 0;

    Thread.sleep(1000);
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    // when
    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        try {
          couponService.issue(couponId, userId);
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();
    executor.shutdown();

    // then
    final Coupon coupon = couponRepository.findById(couponId).orElseThrow();
    assertEquals(expectedCouponQuantity, coupon.getQuantity());
  }


}
