package com.couponify.coupondomain.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.CouponStatus;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CouponTest {

    final LocalDateTime issueStartAt = LocalDateTime.now().plusDays(1);
    final LocalDateTime issueEndAt = LocalDateTime.now().plusDays(2);
    final private String name = "샘플 쿠폰";
    final private CouponStatus status = CouponStatus.AVAILABLE;
    final private int quantity = 100;
    final private int issueQuantity = 1;

    @Test
    @DisplayName("쿠폰 생성시 발급 시작 일시가 미래가 아닐 경우 예외가 발생한다.")
    void constructWithInvalidIssueStartAtThrowsException() {
        // given
        final LocalDateTime issueStartAt = LocalDateTime.now().minusDays(1);

        // when & then
        assertThatThrownBy(() ->
            Coupon.of(name, status, quantity, issueStartAt, issueEndAt))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("쿠폰 생성시 발급 종료 일시가 미래가 아닐 경우 예외가 발생한다.")
    void constructWithInvalidIssueEndAtThrowsException() {
        // given
        final LocalDateTime issueStartAt = LocalDateTime.now().plusDays(1);
        final LocalDateTime issueEndAt = LocalDateTime.now().plusDays(1);

        // when & then
        assertThatThrownBy(() ->
            Coupon.of(name, status, quantity, issueStartAt, issueEndAt))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("쿠폰 생성시 발급 시작 일시와 종료 일시가 같을 경우 예외가 발생한다.")
    void constructWithSameStartAndEndThrowsException() {
        // given
        final LocalDateTime issueEndAt = LocalDateTime.now().minusDays(1);

        // when & then
        assertThatThrownBy(() ->
            Coupon.of(name, status, quantity, issueStartAt, issueEndAt))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("쿠폰 발급시 발급 가능한 기간이 아닐 경우 예외가 발생한다.")
    void issueWithExpiredPeriodThrowsException() {
        // given
        Clock mockClock = mock(Clock.class);
        LocalDateTime fixedNow = LocalDateTime.now();
        when(mockClock.instant()).thenReturn(fixedNow.atZone(ZoneId.systemDefault()).toInstant());
        when(mockClock.getZone()).thenReturn(ZoneId.systemDefault());

        final LocalDateTime issueStartAt = LocalDateTime.now(mockClock).plusSeconds(1);
        final LocalDateTime issueEndAt = issueStartAt.plusSeconds(1);

        Coupon coupon = Coupon.of(name, status, quantity, issueStartAt, issueEndAt);

        LocalDateTime afterPeriod = fixedNow.plusSeconds(3);
        when(mockClock.instant()).thenReturn(
            afterPeriod.atZone(ZoneId.systemDefault()).toInstant());

        // when & then
        assertThatThrownBy(() ->
            coupon.issue(issueQuantity))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("쿠폰 발급시 잔여 수량이 발급 수량보다 부족할 경우 예외가 발생한다.")
    void issueWithInsufficientQuantityThrowsException() {
        // given
        final int quantity = 1;
        Coupon coupon = Coupon.of(name, status, quantity, issueStartAt, issueEndAt);

        // when & then
        assertThatThrownBy(() ->
            coupon.issue(quantity)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("쿠폰 발급시 쿠폰이 '발급 가능' 상태가 아닐 경우 예외가 발생한다.")
    void issueWithNotAvailableStatusThrowException() {
        // given
        final CouponStatus status = CouponStatus.SOLD_OUT;
        Coupon coupon = Coupon.of(name, status, quantity, issueStartAt, issueEndAt);

        // when & then
        assertThatThrownBy(() ->
            coupon.issue(quantity)).isInstanceOf(IllegalArgumentException.class);
    }

}
