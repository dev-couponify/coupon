package com.couponify.couponapi.persentation.request;

import com.couponify.coupondomain.domain.CouponStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponCreateRequest {

  @NotBlank(message = "쿠폰 이름은 필수입니다.")
  private String name;
  @NotNull(message = "쿠폰 상태는 필수입니다.")
  private CouponStatus status;
  @Positive(message = "수량은 0보다 커야 합니다.")
  private Integer quantity;

}
