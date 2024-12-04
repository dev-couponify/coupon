package com.sparta.order.couponapi.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

  private String statusName;
  private int code;
  private String message;
  private T data;

  private ApiResponse(HttpStatus status, String message, T data) {
    this.statusName = status.name();
    this.code = status.value();
    this.message = message;
    this.data = data;
  }

  public static ApiResponse<Void> error(HttpStatus status, String message) {
    return new ApiResponse<>(status, message, null);
  }

  public static <T> ApiResponse<T> error(HttpStatus status, String message, T data) {
    return new ApiResponse<>(status, message, data);
  }

  public static <T> ApiResponse<T> created(T data) {
    return new ApiResponse<>(HttpStatus.CREATED, null, data);
  }

  public static <T> ApiResponse<T> ok(T data) {
    return new ApiResponse<>(HttpStatus.OK, null, data);
  }

  public static ApiResponse<Void> ok() {
    return new ApiResponse<>(HttpStatus.OK, null, null);
  }

}
