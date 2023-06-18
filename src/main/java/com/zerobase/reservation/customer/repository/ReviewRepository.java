package com.zerobase.reservation.customer.repository;

import com.zerobase.reservation.customer.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findAllByMarketId(long marketId);

    ReviewEntity findById(long id);

}
