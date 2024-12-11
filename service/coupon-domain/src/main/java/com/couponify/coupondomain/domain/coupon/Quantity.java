package com.couponify.coupondomain.domain.coupon;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quantity {

  private int quantity;

  public Quantity(int quantity) {
    validateQuantity(quantity);
    this.quantity = quantity;
  }

  public void decrease(int amount) {
    validateQuantity(this.quantity - amount);
    this.quantity -= amount;
  }

  public void increase(int amount) {
    validateQuantity(this.quantity + amount);
    this.quantity += amount;
  }

  public boolean isZero() {
    return this.quantity == 0;
  }

  public boolean isGreaterThanOrEqualTo(int quantityToCompare) {
    return this.quantity >= quantityToCompare;
  }

  private void validateQuantity(int quantity) {
    if (isInvalidQuantity(quantity)) {
      throw new IllegalArgumentException("쿠폰 수량은 0 이상이어야 합니다.");
    }
  }

  private boolean isInvalidQuantity(int quantity) {
    return quantity < 0;
  }

}
