package com.couponify.couponapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class BusinessExceptionHandler {

  @ExceptionHandler({
      HttpRequestMethodNotSupportedException.class,
      HttpMediaTypeNotSupportedException.class,
      HttpMediaTypeNotAcceptableException.class,
      MissingPathVariableException.class,
      MissingServletRequestParameterException.class,
      MissingServletRequestPartException.class,
      ServletRequestBindingException.class,
      TypeMismatchException.class,
      HttpMessageNotReadableException.class,
      MethodArgumentNotValidException.class,
      NoHandlerFoundException.class
  })
  public ResponseEntity<ErrorResponse> badRequestException(
      Exception exception,
      HttpServletRequest request
  ) {
    ErrorResponse errorResponse = ErrorResponse.fromBadRequestException(request, exception);

    log.error("Bad Request", exception);

    return ResponseEntity.status(HttpStatus.valueOf(errorResponse.statusCode()))
        .body(errorResponse);
  }


  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> internalServerErrorException(
      Exception exception,
      HttpServletRequest request
  ) {
    ErrorResponse errorResponse = ErrorResponse.fromInternalServerErrorException(request,
        exception);

    log.error("Internal Server Error", exception);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
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
