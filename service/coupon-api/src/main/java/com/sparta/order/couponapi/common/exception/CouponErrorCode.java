package com.sparta.order.couponapi.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CouponErrorCode {
  TEST_ERROR(HttpStatus.NOT_FOUND, "테스트");

  private final HttpStatus status;
  private final String message;

  CouponErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
