package com.couponify.couponapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CouponErrorCode {
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다. : [%s]"),
    COUPON_ALREADY_ISSUED(HttpStatus.BAD_REQUEST, "이미 쿠폰을 발급받은 사용자입니다."),
    TRANSACTION_COMMIT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 커밋에 실패했습니다. : [%s]");

    private final HttpStatus status;
    private final String message;

    CouponErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
