package com.sparta.order.couponapi.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BusinessExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> unexpectedException(
      Exception exception,
      HttpServletRequest request
  ) {
    if (exception instanceof BusinessException) {
      throw (BusinessException) exception;
    }

    ErrorResponse errorResponse = ErrorResponse.fromGeneralException(request, exception);

    log.error("Exception occurred: ", exception);

    return ResponseEntity.status(HttpStatus.valueOf(errorResponse.statusCode()))
        .body(errorResponse);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> businessException(
      BusinessException exception,
      HttpServletRequest request
  ) {
    ErrorResponse errorResponse = ErrorResponse.fromBusinessException(request, exception);

    log.error(errorResponse.toString());

    return ResponseEntity.status(exception.getStatus())
        .body(errorResponse);
  }

}
