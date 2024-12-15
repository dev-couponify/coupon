package com.couponify.coupondomain.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(TestConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CouponRepositoryTest {

  @Autowired
  private CouponRepository couponRepository;

  @Test
  @DisplayName("쿠폰을 저장할 수 있다.")
  void saveCoupon() {
    // given
    Coupon coupon = CouponFixture.createCoupon();

    // when
    Coupon savedCoupon = couponRepository.save(coupon);

    // then
    assertNotNull(savedCoupon.getId());
  }

  @Test
  @DisplayName("저장된 쿠폰 ID로 쿠폰을 조회할 수 있다.")
  void findCouponById() {
    // given
    Coupon coupon = CouponFixture.createCoupon();
    Coupon savedCoupon = couponRepository.save(coupon);

    // when
    Optional<Coupon> foundCoupon = couponRepository.findById(savedCoupon.getId());

    // then
    assertTrue(foundCoupon.isPresent());
    assertEquals(savedCoupon.getId(), foundCoupon.get().getId());
  }

  @Test
  @DisplayName("기한이 만료된 쿠폰 리스트를 조회할 수 있다.")
  void findExpiredCoupons() {
    // given
    Coupon coupon = CouponFixture.createCoupon();
    Coupon savedCoupon = couponRepository.save(coupon);

    Clock mockClock = mock(Clock.class);
    LocalDateTime expiredPeriod = LocalDateTime.now().plusDays(3);
    when(mockClock.instant()).thenReturn(expiredPeriod.atZone(ZoneId.systemDefault()).toInstant());
    when(mockClock.getZone()).thenReturn(ZoneId.systemDefault());

    // when
    List<Coupon> expiredCoupons = couponRepository.findExpiredCoupons(LocalDateTime.now(mockClock));

    // then
    assertFalse(expiredCoupons.isEmpty());
    assertEquals(savedCoupon.getId(), expiredCoupons.get(0).getId());
  }


}
