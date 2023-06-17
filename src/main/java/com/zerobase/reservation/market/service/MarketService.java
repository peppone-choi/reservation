package com.zerobase.reservation.market.service;

import com.zerobase.reservation.market.entity.MarketEntity;
import com.zerobase.reservation.market.model.MarketAdd;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface MarketService {

    MarketEntity addMarket(MarketAdd marketAdd);

}
