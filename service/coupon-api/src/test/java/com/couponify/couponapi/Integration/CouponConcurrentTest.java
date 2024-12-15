package com.couponify.couponapi.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.couponify.couponapi.application.CouponService;
import com.couponify.couponapi.presentation.request.CouponCreateRequest;
import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.CouponStatus;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import com.couponify.coupondomain.domain.issuedCoupon.repository.IssuedCouponRepository;
import java.time.LocalDateTime;
import java.util.Optional;
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
    final int threadCount = 100;
    final CouponCreateRequest request = new CouponCreateRequest(
        "샘플 쿠폰",
        CouponStatus.AVAILABLE,
        100,
        LocalDateTime.now().plusSeconds(1),
        LocalDateTime.now().plusDays(3)
    );
    final Long couponId = couponRepository.save(CouponCreateRequest.toDomain(request)).getId();
    final Long userId = 1L;
    final int expectedCouponQuantity = 0;

    Thread.sleep(1000);

    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

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

    final Optional<Coupon> coupon = couponRepository.findById(couponId);
    assertEquals(expectedCouponQuantity, coupon.get().getQuantity().getQuantity());
  }


}
