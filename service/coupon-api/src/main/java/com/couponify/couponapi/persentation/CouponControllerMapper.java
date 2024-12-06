package com.couponify.couponapi.persentation;

import com.couponify.couponapi.application.dto.CouponCreateDto;
import com.couponify.couponapi.persentation.request.CouponCreateRequest;

public class CouponControllerMapper {

  public static CouponCreateDto toCouponCreateDto(CouponCreateRequest couponCreateRequest) {
    return new CouponCreateDto(
        couponCreateRequest.getName(),
        couponCreateRequest.getStatus(),
        couponCreateRequest.getQuantity()
    );
  }

}
