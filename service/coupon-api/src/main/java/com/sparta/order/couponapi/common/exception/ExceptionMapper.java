package com.sparta.order.couponapi.common.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

// TODO enum으로 변경
public class ExceptionMapper {

  public static HttpStatus toHttpStatus(Exception exception) {
    if (exception instanceof HttpRequestMethodNotSupportedException) {
      return HttpStatus.METHOD_NOT_ALLOWED;
    } else if (exception instanceof HttpMediaTypeNotSupportedException) {
      return HttpStatus.UNSUPPORTED_MEDIA_TYPE;
    } else if (exception instanceof HttpMediaTypeNotAcceptableException) {
      return HttpStatus.NOT_ACCEPTABLE;
    } else if (exception instanceof MissingPathVariableException) {
      return HttpStatus.BAD_REQUEST;
    } else if (exception instanceof MissingServletRequestParameterException) {
      return HttpStatus.BAD_REQUEST;
    } else if (exception instanceof MissingServletRequestPartException) {
      return HttpStatus.BAD_REQUEST;
    } else if (exception instanceof ServletRequestBindingException) {
      return HttpStatus.BAD_REQUEST;
    } else if (exception instanceof TypeMismatchException) {
      return HttpStatus.BAD_REQUEST;
    } else if (exception instanceof HttpMessageNotReadableException) {
      return HttpStatus.BAD_REQUEST;
    } else if (exception instanceof MethodArgumentNotValidException) {
      return HttpStatus.BAD_REQUEST;
    } else if (exception instanceof NoHandlerFoundException) {
      return HttpStatus.NOT_FOUND;
    } else if (exception instanceof AsyncRequestTimeoutException) {
      return HttpStatus.SERVICE_UNAVAILABLE;
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }

}
