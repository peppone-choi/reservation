package com.zerobase.reservation.customer.service;

import com.zerobase.reservation.customer.entity.ReservationEntity;
import com.zerobase.reservation.customer.model.AddReservation;
import com.zerobase.reservation.market.entity.MarketEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CustomerService {
    List<MarketEntity> getMarketList(String name);

    MarketEntity getMarketInfo(long id);

    ResponseEntity addReservation(AddReservation addReservation);
}
