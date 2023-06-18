package com.zerobase.reservation.customer.service.Impl;

import com.zerobase.reservation.customer.entity.ReservationEntity;
import com.zerobase.reservation.customer.entity.ReviewEntity;
import com.zerobase.reservation.customer.model.AddReservation;
import com.zerobase.reservation.customer.model.AddReview;
import com.zerobase.reservation.customer.repository.ReservationRepository;
import com.zerobase.reservation.customer.repository.ReviewRepository;
import com.zerobase.reservation.customer.service.CustomerService;
import com.zerobase.reservation.market.entity.MarketEntity;
import com.zerobase.reservation.market.repository.MarketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final MarketRepository marketRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;


    @Override
    public List<MarketEntity> getMarketList(String name) {
        return marketRepository.findAllByMarketNameContaining(name);
    }

    @Override
    public MarketEntity getMarketInfo(long id) {
        return marketRepository.findById(id);
    }

    @Override
    public ResponseEntity<?> addReservation(AddReservation addReservation) {

        ReservationEntity reservation = ReservationEntity.builder()
                .marketId(addReservation.getMarketId())
                .reserveDate(addReservation.getReserveDate())
                .reserveTime(addReservation.getReserveTime())
                .reservationPhone(addReservation.getReservationPhone())
                .approve(false)
                .arrive(false)
                .build();

        reservationRepository.save(reservation);

        return ResponseEntity.ok().body(reservation);
    }

    @Override
    public List<ReviewEntity> findAllReview() {
        return reviewRepository.findAll();
    }

    @Override
    public List<ReviewEntity> findByMarketId(long id) {
        return reviewRepository.findAllByMarketId(id);
    }

    @Override
    public ReviewEntity findReviewById(long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public ResponseEntity addReview(AddReview addReview) {
        ReviewEntity review = ReviewEntity.builder()
                .marketId(addReview.getMarketId())
                .reviewSubject(addReview.getReviewSubject())
                .reviewDesc(addReview.getReviewDesc())
                .build();

        reviewRepository.save(review);

        return ResponseEntity.ok().body(review);
    }

}
