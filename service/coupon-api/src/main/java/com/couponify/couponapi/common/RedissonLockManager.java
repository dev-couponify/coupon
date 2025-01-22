package com.couponify.couponapi.common;

import com.couponify.couponapi.exception.CouponErrorCode;
import com.couponify.couponapi.exception.CouponException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedissonLockManager {

    private final RedissonClient redissonClient;

    public void executeLock(
        String lockName, long waitSeconds, long leaseSeconds, Runnable logic) {
        RLock lock = redissonClient.getLock(lockName);
        try {
            if (!lock.tryLock(waitSeconds, leaseSeconds, TimeUnit.SECONDS)) {
                throw new CouponException(CouponErrorCode.LOCK_ACQUISITION_FAILED);
            }
            logic.run();
        } catch (InterruptedException e) {
            throw new CouponException(CouponErrorCode.LOCK_ACQUISITION_FAILED);
        } finally {
            lock.unlock();
        }
    }

    public void executeMultipleLocks(
        List<String> lockNames, long waitSeconds, long leaseSeconds, int retryCount,
        Runnable logic) {
        for (int attempt = 0; attempt < retryCount; attempt++) {
            List<RLock> locks = new ArrayList<>();
            try {
                if (tryAcquireLocks(lockNames, locks, waitSeconds, leaseSeconds)) {
                    logic.run();
                    return;
                }
            } catch (InterruptedException e) {
                throw new CouponException(CouponErrorCode.LOCK_ACQUISITION_FAILED);
            } finally {
                releaseLocks(locks);
            }
        }
        throw new CouponException(CouponErrorCode.LOCK_ACQUISITION_FAILED);
    }

    private boolean tryAcquireLocks(List<String> lockNames, List<RLock> locks, long waitSeconds,
        long leaseSeconds) throws InterruptedException {
        for (String lockName : lockNames) {
            RLock lock = redissonClient.getLock(lockName);
            locks.add(lock);

            if (!lock.tryLock(waitSeconds, leaseSeconds, TimeUnit.SECONDS)) {
                return false;
            }
        }
        return true;
    }

    private void releaseLocks(List<RLock> locks) {
        for (RLock lock : locks) {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
