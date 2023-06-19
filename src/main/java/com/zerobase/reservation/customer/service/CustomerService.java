package com.zerobase.reservation.customer.service;

import com.zerobase.reservation.customer.entity.ReviewEntity;
import com.zerobase.reservation.customer.model.AddReservation;
import com.zerobase.reservation.customer.model.AddReview;
import com.zerobase.reservation.market.entity.MarketEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    ResponseEntity addReview(AddReview addReview);

    List<MarketEntity> getMarketList();

    MarketEntity getMarketInfo(long id);

    ResponseEntity addReservation(AddReservation addReservation);

    List<ReviewEntity> findAllReview();

    List<ReviewEntity> findByMarketId(long id);

    ReviewEntity findReviewById(long id);

    ResponseEntity<?> enterKiosk(long id);
}
