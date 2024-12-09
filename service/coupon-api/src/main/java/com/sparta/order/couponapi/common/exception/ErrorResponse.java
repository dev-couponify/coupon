package com.sparta.order.couponapi.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
    String method,
    String path,
    String statusCode,
    String message,
    LocalDateTime timestamp
) {

  public static ErrorResponse fromBadRequestException(
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

  public static ErrorResponse fromInternalServerErrorException(
      HttpServletRequest request,
      Exception exception
  ) {
    return new ErrorResponse(
        request.getMethod(),
        request.getRequestURI(),
        HttpStatus.INTERNAL_SERVER_ERROR.name(),
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
