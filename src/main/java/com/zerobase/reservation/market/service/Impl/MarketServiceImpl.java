package com.zerobase.reservation.market.service.Impl;

import com.zerobase.reservation.customer.entity.ReservationEntity;
import com.zerobase.reservation.customer.repository.ReservationRepository;
import com.zerobase.reservation.market.entity.MarketEntity;
import com.zerobase.reservation.market.model.MarketAdd;
import com.zerobase.reservation.market.repository.MarketRepository;
import com.zerobase.reservation.market.service.MarketService;
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
    @Override
    public ResponseEntity<?> addMarket(MarketAdd marketAdd) {

        String apikey = "79EA3263-C9B7-341A-BEE5-00E50ABC1EA7";
        String searchType = "parcel";
        String searchAddr = marketAdd.getMarketAddress();
        String epsg = "epsg:4326";

        StringBuilder sb = new StringBuilder("https://api.vworld.kr/req/address");
        sb.append("?service=address");
        sb.append("&request=getCoord");
        sb.append("&format=json");
        sb.append("&crs=" + epsg);
        sb.append("&key=" + apikey);
        sb.append("&type=" + searchType);
        sb.append("&address=" + URLEncoder.encode(searchAddr, StandardCharsets.UTF_8));

        JSONObject jspoitn;
        try {
            URL url = new URL(sb.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

            JSONParser jspa = new JSONParser();
            JSONObject jsob = (JSONObject) jspa.parse(reader);
            JSONObject jsrs = (JSONObject) jsob.get("response");
            JSONObject jsResult = (JSONObject) jsrs.get("result");
            jspoitn = (JSONObject) jsResult.get("point");

        } catch (IOException | java.io.IOException |
                 org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }

        // TODO : 주소를 좌표로 바꾸기
        MarketEntity market = MarketEntity.builder()
                .marketName(marketAdd.getMarketName())
                .marketDesc(marketAdd.getMarketDesc())
                .marketAddress(marketAdd.getMarketAddress())
                .marketX((Double) jspoitn.get("x"))
                .marketY((Double) jspoitn.get("y"))
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
