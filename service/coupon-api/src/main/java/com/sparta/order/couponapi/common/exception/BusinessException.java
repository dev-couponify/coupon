package com.sparta.order.couponapi.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BusinessException extends RuntimeException {

  protected HttpStatus status;
  private final String message;

  public BusinessException(HttpStatus status, String message) {
    super(message);
    this.status = status;
    this.message = message;
  }

  public BusinessException(HttpStatus status, String message, Object... args) {
    super(formattingErrorMessage(message, args));
    this.status = status;
    this.message = formattingErrorMessage(message, args);
  }

  private static String formattingErrorMessage(String message, Object... objects) {
    return message.formatted(objects);
  }


}
