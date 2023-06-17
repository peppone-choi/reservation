package com.zerobase.reservation.market.service.Impl;

import com.zerobase.reservation.market.entity.MarketEntity;
import com.zerobase.reservation.market.model.MarketAdd;
import com.zerobase.reservation.market.service.MarketService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MarketServiceImpl implements MarketService {
    @Override
    public MarketEntity addMarket(MarketAdd marketAdd) {
        // TODO : 주소를 좌표로 바꾸기
        return MarketEntity.builder()
                .marketName(marketAdd.getMarketName())
                .marketDesc(marketAdd.getMarketDesc())
//                .marketX()
//                .marketY()
                .build();
    }
}
