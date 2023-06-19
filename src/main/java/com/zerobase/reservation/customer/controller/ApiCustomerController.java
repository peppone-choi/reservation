package com.zerobase.reservation.customer.controller;

import com.zerobase.reservation.customer.entity.ReviewEntity;
import com.zerobase.reservation.customer.model.AddReservation;
import com.zerobase.reservation.customer.model.AddReview;
import com.zerobase.reservation.customer.service.CustomerService;
import com.zerobase.reservation.market.entity.MarketEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApiCustomerController {
    private final CustomerService customerService;

    /**
     * 등록 매장 리스트 확인 메소드
     * @return
     */
    @GetMapping("/api/customer/")
    public List<MarketEntity> getMarketList() {
        return customerService.getMarketList();
    }


    /**
     * 특정 매장에 대한 정보 확인 메소드
     * @param id
     * @return
     */
    @GetMapping("/api/customer/{id}")
    public MarketEntity getMarketInfo(@PathVariable long id) {
        return customerService.getMarketInfo(id);
    }

    /**
     * 주문 예약 메소드
     * @param addReservation
     * @return
     */
    @PostMapping("/api/customer/reservation")
    public ResponseEntity<?> addReservation(@RequestBody AddReservation addReservation) {
        return customerService.addReservation(addReservation);
    }

    /**
     * 리뷰 작성 메소드
     * @param addReview
     * @return
     */
    @PostMapping("/api/review")
    public ResponseEntity<?> addReview(@RequestBody @Valid AddReview addReview) {
        return customerService.addReview(addReview);
    }

    /**
     * 리뷰 리스트 열람 메소드
     * @return
     */
    @GetMapping("/api/review/all")
    public List<ReviewEntity> findAllReview() {
        return customerService.findAllReview();
    }

    /**
     * 매장별 리뷰 열람 메소드
     * @param id
     * @return
     */
    @GetMapping("/api/review/market/{id}")
    public List<ReviewEntity> findByMarketId(@PathVariable long id) {
        return customerService.findByMarketId(id);
    }

    /**
     * 리뷰 게시글 확인
     * @param id
     * @return
     */
    @GetMapping("/api/review/{id}")
    public ReviewEntity findReviewById(@PathVariable long id) {
        return customerService.findReviewById(id);
    }

    /**
     * 매장내 키오스크에서의 입장 확인 메소드
     * @param id
     * @return
     */
    @PatchMapping("/api/kiosk/{id}")
    public ResponseEntity<?> enterKiosk(@PathVariable long id) {
        return customerService.enterKiosk(id);
    }

}
