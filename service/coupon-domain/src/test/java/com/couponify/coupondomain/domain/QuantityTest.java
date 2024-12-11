package com.couponify.coupondomain.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.couponify.coupondomain.domain.coupon.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuantityTest {

  final private int initialValue = 1;

  @Test
  @DisplayName("수량 생성시 음수값을 넣을 경우 예외가 발생한다.")
  void constructWithNegativeValueThrowsException() {
    // given
    final int wrongValue = -10;

    // when & then
    assertThatThrownBy(() -> new Quantity(wrongValue))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("수량 감소시 결과가 음수값일 경우 예외가 발생한다.")
  void decreaseWithNegativeResultThrowsException() {
    // given
    final int decreaseValue = 10;
    final Quantity quantity = new Quantity(initialValue);

    // when & then
    assertThatThrownBy(() -> quantity.decrease(decreaseValue))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("수량 증가시 결과가 음수값일 경우 예외가 발생한다.")
  void increaseWithNegativeResultThrowsException() {
    // given
    final int increaseValue = -10;
    final Quantity quantity = new Quantity(initialValue);

    // when & then
    assertThatThrownBy(() -> quantity.increase(increaseValue))
        .isInstanceOf(IllegalArgumentException.class);
  }

}
