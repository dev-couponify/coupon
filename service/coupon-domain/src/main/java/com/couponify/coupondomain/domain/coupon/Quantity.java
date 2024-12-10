package com.couponify.coupondomain.domain.coupon;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quantity {

  private int quantity;

  public Quantity(int quantity) {
    validateValue(quantity);
    this.quantity = quantity;
  }

  public void subtract(int value) {
    validateValue(this.quantity - value);
    this.quantity -= value;
  }

  public void add(int value) {
    validateValue(this.quantity + value);
    this.quantity += value;
  }

  private void validateValue(int value) {
    if (isInvalidQuantity(value)) {
      throw new IllegalArgumentException("쿠폰 수량은 0 이상이어야 합니다.");
    }
  }

  private boolean isInvalidQuantity(int value) {
    return value < 0;
  }

}
