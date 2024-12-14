package com.couponify.couponapi.unit.application;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.couponify.couponapi.application.CouponService;
import com.couponify.couponapi.exception.CouponErrorCode;
import com.couponify.couponapi.exception.CouponException;
import com.couponify.couponapi.presentation.request.CouponCreateRequest;
import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.CouponStatus;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import com.couponify.coupondomain.domain.issuedCoupon.IssuedCoupon;
import com.couponify.coupondomain.domain.issuedCoupon.repository.IssuedCouponRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

  @Mock
  private Coupon coupon;
  @Mock
  private IssuedCoupon issuedCoupon;
  @Mock
  private CouponRepository couponRepository;
  @Mock
  private IssuedCouponRepository issuedCouponRepository;
  @InjectMocks
  private CouponService couponService;

  @Test
  @DisplayName("쿠폰을 생성할 수 있다.")
  void createCoupon() {
    // given
    final Long expectedCouponId = 1L;
    final CouponCreateRequest request = new CouponCreateRequest
        (
            "샘플 쿠폰",
            CouponStatus.AVAILABLE,
            100,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2)
        );

    // mocking
    given(couponRepository.save(any(Coupon.class))).willReturn(coupon);
    given(coupon.getId()).willReturn(expectedCouponId);

    // when
    Long savedCouponId = couponService.create(request);

    // then
    assertEquals(expectedCouponId, savedCouponId);
    verify(couponRepository).save(any(Coupon.class));
  }

  @Test
  @DisplayName("쿠폰을 발급할 수 있다.")
  void issueCoupon() {
    // given
    final Long userId = 1L;
    final Long couponId = 1L;
    final Long expectedIssuedCouponId = 1L;

    given(couponRepository.findById(any(Long.class))).willReturn(Optional.of(coupon));
    given(issuedCouponRepository.save(any(IssuedCoupon.class))).willReturn(issuedCoupon);
    given(issuedCoupon.getId()).willReturn(expectedIssuedCouponId);
    doNothing().when(coupon).issue(anyInt());

    // when
    Long savedIssuedCouponId = couponService.issue(userId, couponId);

    // then
    assertEquals(expectedIssuedCouponId, savedIssuedCouponId);
    verify(coupon).issue(anyInt());
    verify(couponRepository).findById(anyLong());
    verify(issuedCouponRepository).save(any(IssuedCoupon.class));
  }

  @Test
  @DisplayName("존재하지 않는 쿠폰을 발급할 경우 예외가 발생한다.")
  void issueNotExistingCouponThrowsException() {
    // given
    final Long userId = 1L;
    final Long couponId = 1L;

    given(couponRepository.findById(any(Long.class))).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> couponService.issue(userId, couponId))
        .isInstanceOf(CouponException.class)
        .hasFieldOrPropertyWithValue("errorCode", CouponErrorCode.COUPON_NOT_FOUND);

    verify(couponRepository).findById(anyLong());
    verifyNoMoreInteractions(couponRepository);
    verifyNoMoreInteractions(issuedCouponRepository);
    verifyNoMoreInteractions(coupon);
  }

  @Test
  @DisplayName("만료된 쿠폰이 있을 경우 쿠폰을 만기시킬 수 있다.")
  void expireCoupons() {
    // given
    Coupon coupon1 = mock(Coupon.class);
    Coupon coupon2 = mock(Coupon.class);
    List<Coupon> expiredCoupons = Arrays.asList(coupon1, coupon2);

    given(couponRepository.findExpiredCoupons(any(LocalDateTime.class)))
        .willReturn(expiredCoupons);
    doNothing().when(coupon1).expire();
    doNothing().when(coupon2).expire();

    // when
    couponService.expire();

    // then
    verify(couponRepository).findExpiredCoupons(any(LocalDateTime.class));
    verify(coupon1).expire();
    verify(coupon2).expire();
  }

  @Test
  @DisplayName("만료된 쿠폰이 없을 경우 아무 작업도 하지 않는다.")
  void expireNoCoupons() {
    // given
    given(couponRepository.findExpiredCoupons(any(LocalDateTime.class))).willReturn(
        Collections.emptyList());

    // when
    couponService.expire();

    verify(couponRepository).findExpiredCoupons(any(LocalDateTime.class));
    verifyNoMoreInteractions(couponRepository);
  }

}
