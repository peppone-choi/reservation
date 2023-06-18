package com.zerobase.reservation.market.service.Impl;

import com.zerobase.reservation.customer.entity.ReservationEntity;
import com.zerobase.reservation.customer.repository.ReservationRepository;
import com.zerobase.reservation.market.entity.MarketEntity;
import com.zerobase.reservation.market.model.MarketAdd;
import com.zerobase.reservation.market.repository.MarketRepository;
import com.zerobase.reservation.market.service.MarketService;
import com.zerobase.reservation.util.GeoCoding;
import com.zerobase.reservation.util.ResponseMessage;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {

    private final MarketRepository marketRepository;
    private final ReservationRepository reservationRepository;
    private final GeoCoding geoCoding;

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



}
