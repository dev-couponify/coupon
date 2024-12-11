package com.couponify.coupondomain.infrastructure.jpa.coupon;

import com.couponify.coupondomain.domain.coupon.Coupon;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCouponRepository extends JpaRepository<Coupon, Long> {

  List<Coupon> findAllByIssueEndAtBefore(LocalDateTime now);

}
