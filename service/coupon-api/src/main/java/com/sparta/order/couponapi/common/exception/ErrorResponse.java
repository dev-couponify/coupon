package com.sparta.order.couponapi.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

public record ErrorResponse(
    String method,
    String path,
    String statusCode,
    String message,
    LocalDateTime timestamp
) {

  public static ErrorResponse fromGeneralException(
      HttpServletRequest request,
      Exception exception
  ) {
    return new ErrorResponse(
        request.getMethod(),
        request.getRequestURI(),
        ExceptionMapper.toHttpStatus(exception).name(),
        exception.getMessage(),
        LocalDateTime.now()
    );
  }

  public static ErrorResponse fromBusinessException(
      HttpServletRequest request,
      BusinessException exception
  ) {
    return new ErrorResponse(
        request.getMethod(),
        request.getRequestURI(),
        exception.getCode(),
        exception.getMessage(),
        LocalDateTime.now()
    );
  }


}
