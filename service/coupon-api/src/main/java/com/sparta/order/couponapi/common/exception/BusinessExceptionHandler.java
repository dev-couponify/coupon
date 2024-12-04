package com.sparta.order.couponapi.common.exception;

import com.sparta.order.couponapi.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BusinessExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ApiResponse<Void> handleBusinessException(BusinessException ex) {
    log.error(ex.getStatus().name(), ex.getMessage());
    return ApiResponse.error(ex.getStatus(), ex.getMessage());
  }

  @ExceptionHandler({Exception.class, RuntimeException.class})
  public ApiResponse<Void> handleAllExceptions(Exception ex) {
    ex.printStackTrace();
    return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }

}
