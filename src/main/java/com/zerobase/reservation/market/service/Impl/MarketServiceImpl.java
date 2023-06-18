package com.zerobase.reservation.market.service.Impl;

import com.zerobase.reservation.customer.entity.ReservationEntity;
import com.zerobase.reservation.customer.repository.ReservationRepository;
import com.zerobase.reservation.market.entity.MarketEntity;
import com.zerobase.reservation.market.model.MarketAdd;
import com.zerobase.reservation.market.repository.MarketRepository;
import com.zerobase.reservation.market.service.MarketService;
import com.zerobase.reservation.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {

    private final MarketRepository marketRepository;
    private final ReservationRepository reservationRepository;
    @Override
    public ResponseEntity<?> addMarket(MarketAdd marketAdd) {
        // TODO : 주소를 좌표로 바꾸기
        MarketEntity market = MarketEntity.builder()
                .marketName(marketAdd.getMarketName())
                .marketDesc(marketAdd.getMarketDesc())
                .marketAddress(marketAdd.getMarketAddress())
//                .marketX()
//                .marketY()
                .build();

        marketRepository.save(market);

        return ResponseEntity.ok().body(market);
    }

    @Override
    public List<ReservationEntity> getReservation(long id) {
        return reservationRepository.findAllByMarketId(id);
    }

    @Override
    public ResponseEntity approve(long id) {

        ReservationEntity reservation = reservationRepository.findById(id);

        if(reservation.isApprove()) {
            return ResponseEntity.badRequest().body("이미 승인 된 주문입니다.");
        }

        if(reservation == null) {
            ResponseEntity.notFound().build();
        }

        reservation.setApprove(true);

        reservationRepository.save(reservation);

        return ResponseEntity.ok().body(reservation);
    }

    @Override
    public ResponseEntity<?> deny(long id) {
        ReservationEntity reservation = reservationRepository.findById(id);

        if (reservation.isApprove()) {
            return ResponseEntity.badRequest().body("이미 승인 된 주문입니다.");
        }

        if (reservation == null) {
            ResponseEntity.notFound().build();
        }

        reservationRepository.delete(reservation);

        return ResponseEntity.ok().body("["+ id + "] 해당 주문의 승인이 정상적으로 거절되었습니다.");
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


}
