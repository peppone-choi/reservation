package com.zerobase.reservation.customer.repository;

import com.zerobase.reservation.customer.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findAllByMarketId(long marketId);

    ReservationEntity findById(long id);
}
