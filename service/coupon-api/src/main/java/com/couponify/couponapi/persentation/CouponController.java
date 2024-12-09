package com.couponify.couponapi.persentation;

import com.couponify.couponapi.application.CouponService;
import com.couponify.couponapi.application.dto.CouponCreateDto;
import com.couponify.couponapi.persentation.request.CouponCreateRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

  private final CouponService couponService;

  @PostMapping
  public ResponseEntity<Void> create(@Valid @RequestBody CouponCreateRequest couponCreateRequest) {
    CouponCreateDto couponCreateDto = CouponControllerMapper.toCouponCreateDto(couponCreateRequest);
    Long savedCouponId = couponService.create(couponCreateDto);
    return ResponseEntity.created(URI.create("/coupon/" + savedCouponId)).build();
  }

  @PostMapping("/issue/{couponId}")
  public ResponseEntity<Void> issue(@PathVariable(name = "couponId") Long couponId,
      @RequestParam(name = "user-id") Long userId) {
    Long savedIssuedCouponId = couponService.issue(couponId, userId);
    return ResponseEntity.created(URI.create("/issuedCoupon/" + savedIssuedCouponId)).build();
  }

}
