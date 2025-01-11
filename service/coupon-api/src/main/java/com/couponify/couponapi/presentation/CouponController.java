package com.couponify.couponapi.presentation;

import com.couponify.couponapi.application.CouponLockService;
import com.couponify.couponapi.application.CouponService;
import com.couponify.couponapi.presentation.request.CouponCreateRequest;
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
    private final CouponLockService couponLockService;

    @PostMapping
    public ResponseEntity<Void> create(
        @Valid @RequestBody CouponCreateRequest couponCreateRequest) {
        Long savedCouponId = couponService.create(couponCreateRequest);
        return ResponseEntity.created(URI.create("/coupon/" + savedCouponId)).build();
    }

    @PostMapping("/issue/{couponId}")
    public ResponseEntity<Void> issue(
        @PathVariable(name = "couponId") Long couponId,
        @RequestParam(name = "user-id") Long userId) {
        Long savedIssuedCouponId = couponLockService.issueRLock(couponId, userId);
        return ResponseEntity.created(URI.create("/issuedCoupon/" + savedIssuedCouponId)).build();
    }

}
