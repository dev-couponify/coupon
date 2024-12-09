package com.couponify.coupondomain.domain.coupon;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Quantity {

  private int quantity;

  protected Quantity() {
  }

  public Quantity(int quantity) {
    validateValue(quantity);
    this.quantity = quantity;
  }

  private void validateValue(int value) {
    if (value < 0) {
      throw new IllegalArgumentException("쿠폰 수량은 0 이상이어야 합니다.");
    }
  }

  public void subtract(int value) {
    validateValue(this.quantity - value);
    this.quantity -= value;
  }

  public void add(int value) {
    validateValue(this.quantity + value);
    this.quantity += value;
  }

}
