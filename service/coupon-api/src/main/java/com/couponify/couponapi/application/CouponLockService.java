package com.couponify.couponapi.application;

import static com.couponify.couponapi.common.CouponPrefix.LOCK_COUPON_PREFIX;
import static com.couponify.couponapi.common.CouponPrefix.LOCK_ISSUER_PREFIX;

import com.couponify.couponapi.common.RedissonLockManager;
import com.couponify.coupondomain.domain.coupon.repository.CouponRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j(topic = "CouponLockService")
@Service
@RequiredArgsConstructor
public class CouponLockService {

    private final CouponIssueService couponIssueService;
    private final CouponRepository couponRepository;
    private final RedissonLockManager redissonLockManager;

    public void cacheCouponIssuance(Long couponId, Long userId) {
        List<String> lockNames = generateIssuanceLockNames(List.of(couponId));
        redissonLockManager.executeMultipleLocks(lockNames, 10, 5, 3,
            () -> couponIssueService.cacheCouponIssuance(couponId, userId));
    }

    public void persistCouponIssuance() {
        Set<Long> issuedCouponIds = couponIssueService.getIssuedCouponIds();
        List<String> lockNames = generateIssuanceLockNames(issuedCouponIds.stream().toList());
        redissonLockManager.executeMultipleLocks(lockNames, 10, 5, 3,
            couponIssueService::persistCouponIssuance);
    }

    private List<String> generateIssuanceLockNames(List<Long> couponIds) {
        return couponIds.stream()
            .flatMap(couponId -> Stream.of(
                LOCK_COUPON_PREFIX + couponId, LOCK_ISSUER_PREFIX + couponId
            ))
            .toList();
    }

}
