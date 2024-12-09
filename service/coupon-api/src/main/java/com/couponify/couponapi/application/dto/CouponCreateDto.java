package com.couponify.couponapi.application.dto;

import com.couponify.coupondomain.domain.CouponStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponCreateDto {

  private String name;
  private CouponStatus status;
  private Integer quantity;


}
