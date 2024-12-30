package com.couponify.couponapi.application;

import com.couponify.couponapi.exception.CouponErrorCode;
import com.couponify.couponapi.exception.CouponException;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponLockService {

  private final RedissonClient redissonClient;
  private final CouponService couponService;
  private final CouponRepository couponRepository;

  public Long issueRLock(Long couponId, Long userId) {
    RLock lock = redissonClient.getLock("issue:coupon:" + couponId);

    try {
      if (lock.tryLock(10, 5, TimeUnit.SECONDS)) {
        return couponService.issue(couponId, userId);
      } else {
        throw new CouponException(CouponErrorCode.LOCK_ACQUISITION_FAILED);
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
  }

}
