package com.couponify.couponapi.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponSchedulerService {

    private final CouponExpireService couponExpireService;
    private final CouponIssueService couponIssueService;

    @Value("${schedule.use}")
    private boolean useSchedule;

    @Scheduled(cron = "${schedule.cron.expire}")
    public void expireCoupon() {
        if (useSchedule) {
            couponExpireService.expire();
        }
    }

    @Scheduled(cron = "${schedule.cron.issue}")
    public void issueCoupon() {
        if (useSchedule) {
            couponIssueService.persistCouponIssuance();
        }
    }

}
