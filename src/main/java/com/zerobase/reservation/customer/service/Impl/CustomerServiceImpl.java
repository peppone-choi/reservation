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
import com.zerobase.reservation.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final MarketRepository marketRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;


    @Override
    public List<MarketEntity> getMarketList() {
        return marketRepository.findAll();
    }

    @Override
    public MarketEntity getMarketInfo(long id) {
        return marketRepository.findById(id);
    }

    @Override
    public ResponseEntity<?> addReservation(AddReservation addReservation) {

        ReservationEntity reservation = ReservationEntity.builder()
                .marketId(addReservation.getMarketId())
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
    public ResponseEntity<?> enterKiosk(long id) {
        ReservationEntity reservation = reservationRepository.findById(id);

        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }

        if (reservation.getReserveTime().isAfter(LocalDateTime.now().minusMinutes(10))) {
            reservationRepository.delete(reservation);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage.fail("예약 시간 10분 전에 들어오지 않아 예약이 취소되었습니다. 자세한 사항은 매장에 문의하세요."));
        }

        reservation.setArrive(true);
        return ResponseEntity.ok().body(ResponseMessage.success("예약이 성공하였습니다\n" + reservation));
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
