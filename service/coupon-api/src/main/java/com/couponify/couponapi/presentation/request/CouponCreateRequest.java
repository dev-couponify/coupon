package com.couponify.couponapi.presentation.request;

import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.coupon.CouponStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CouponCreateRequest {

    @NotBlank(message = "쿠폰 이름은 필수입니다.")
    private String name;
    @NotNull(message = "쿠폰 상태는 필수입니다.")
    private CouponStatus status;
    @Positive(message = "수량은 0보다 커야 합니다.")
    private int quantity;
    @NotNull(message = "발급 시작 일시는 필수입니다.")
    @Future(message = "발급 시작 일시는 미래여야 합니다.")
    private LocalDateTime issueStartAt;
    @NotNull(message = "발급 종료 일시는 필수입니다.")
    @Future(message = "발급 종료 일시는 미래여야 합니다.")
    private LocalDateTime issueEndAt;

    public static Coupon toDomain(CouponCreateRequest request) {
        return Coupon.of(
            request.name,
            request.status,
            request.getQuantity(),
            request.getIssueStartAt(),
            request.getIssueEndAt());
    }

}
