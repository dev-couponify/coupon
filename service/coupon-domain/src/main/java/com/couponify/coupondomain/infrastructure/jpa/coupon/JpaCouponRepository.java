package com.couponify.coupondomain.infrastructure.jpa.coupon;

import com.couponify.coupondomain.domain.coupon.Coupon;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaCouponRepository extends JpaRepository<Coupon, Long> {

  List<Coupon> findAllByIssueEndAtBefore(LocalDateTime now);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select c from Coupon c where c.id = :id")
  Optional<Coupon> findByIdForUpdate(@Param("id") Long id);

}
