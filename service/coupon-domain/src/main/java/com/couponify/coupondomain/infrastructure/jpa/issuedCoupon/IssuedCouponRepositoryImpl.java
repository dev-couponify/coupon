package com.couponify.coupondomain.infrastructure.jpa.issuedCoupon;

import com.couponify.coupondomain.domain.coupon.Coupon;
import com.couponify.coupondomain.domain.issuedCoupon.IssuedCoupon;
import com.couponify.coupondomain.domain.issuedCoupon.repository.IssuedCouponRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IssuedCouponRepositoryImpl implements IssuedCouponRepository {

    private final JpaIssuedCouponRepository jpaIssuedCouponRepository;

    @Override
    public List<IssuedCoupon> saveAll(List<IssuedCoupon> issuedCoupons) {
        return jpaIssuedCouponRepository.saveAll(issuedCoupons);
    }

    @Override
    public List<Long> findUserIdsByCoupon(Coupon coupon) {
        return jpaIssuedCouponRepository.findUserIdsByCoupon(coupon);
    }

    @Override
    public IssuedCoupon save(IssuedCoupon issuedCoupon) {
        return jpaIssuedCouponRepository.save(issuedCoupon);
    }

}
