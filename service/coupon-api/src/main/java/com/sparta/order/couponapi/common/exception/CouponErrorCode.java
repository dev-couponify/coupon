package com.sparta.order.couponapi.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CouponErrorCode {
  TEST_EXCEPTION(HttpStatus.NOT_FOUND, "테스트입니다");

  private final HttpStatus status;
  private final String message;

  CouponErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
