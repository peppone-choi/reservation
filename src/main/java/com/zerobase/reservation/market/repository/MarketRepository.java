package com.zerobase.reservation.market.repository;

import com.zerobase.reservation.market.entity.MarketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketRepository extends JpaRepository<MarketEntity, Long> {
   MarketEntity findById(long id);
}
