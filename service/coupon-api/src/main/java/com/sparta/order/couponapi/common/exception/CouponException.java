package com.sparta.order.couponapi.common.exception;

import lombok.Getter;

@Getter
public class CouponException extends BusinessException {

  private final CouponErrorCode errorCode;

  public CouponException(CouponErrorCode code) {
    super(code.getStatus(), code.name(), code.getMessage());
    errorCode = code;
  }

  public CouponException(CouponErrorCode code, Object... args) {
    super(code.getStatus(), code.name(), code.getMessage(), args);
    this.errorCode = code;
  }

}
