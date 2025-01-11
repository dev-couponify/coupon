package com.couponify.coupondomain.domain.coupon;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quantity {

    private int value;

    public Quantity(int value) {
        validateValue(value);
        this.value = value;
    }

    public void decrease(int amount) {
        validateValue(this.value - amount);
        this.value -= amount;
    }

    public void increase(int amount) {
        validateValue(this.value + amount);
        this.value += amount;
    }

    public boolean isZero() {
        return this.value == 0;
    }

    public boolean isGreaterThanOrEqualTo(int valueToCompare) {
        return this.value >= valueToCompare;
    }

    private void validateValue(int value) {
        if (isInvalidValue(value)) {
            throw new IllegalArgumentException("쿠폰 수량은 0 이상이어야 합니다.");
        }
    }

    private boolean isInvalidValue(int value) {
        return value < 0;
    }

}
