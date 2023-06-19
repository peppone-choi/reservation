package com.zerobase.reservation.market.service.Impl;

import com.zerobase.reservation.customer.entity.ReservationEntity;
import com.zerobase.reservation.customer.repository.ReservationRepository;
import com.zerobase.reservation.market.entity.MarketEntity;
import com.zerobase.reservation.market.model.MarketAdd;
import com.zerobase.reservation.market.repository.MarketRepository;
import com.zerobase.reservation.market.service.MarketService;
import com.zerobase.reservation.util.GeoCoding;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {

    private final MarketRepository marketRepository;
    private final ReservationRepository reservationRepository;
    private final GeoCoding geoCoding;

    /**
     * 매장 등록 메소드의 구현체
     * @param marketAdd
     * @return
     */
    @Override
    public ResponseEntity<?> addMarket(MarketAdd marketAdd) {
        String geoString = marketAdd.getMarketAddress().replace(" ", "");
        double[] geoPoint = geoCoding.geoPoint(geoString);

        MarketEntity market = MarketEntity.builder()
                .marketName(marketAdd.getMarketName())
                .marketDesc(marketAdd.getMarketDesc())
                .marketAddress(marketAdd.getMarketAddress())
                .marketX(geoPoint[0])
                .marketY(geoPoint[1])
                .build();

        marketRepository.save(market);

        return ResponseEntity.ok().body(market);
    }

    /**
     * 매장 별 주문 현황 획인 메소드의 구현체
     * @param id
     * @return
     */
    @Override
    public List<ReservationEntity> getReservation(long id) {
        return reservationRepository.findAllByMarketId(id);
    }

    /**
     * 주문 승인 메소드의 구현체
     * @param id
     * @return
     */
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

    /**
     * 주문 거절 메소드의 구현체
     * @param id
     * @return
     */
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

        return ResponseEntity.ok().body("["+ id + "] 해당 주문의 승인이 정상적으로 거절되었습니다. 이 주문은 데이터에서 삭제됩니다.");
    }

}
