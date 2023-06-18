package com.zerobase.reservation.customer.service.Impl;

import com.zerobase.reservation.customer.entity.ReservationEntity;
import com.zerobase.reservation.customer.model.AddReservation;
import com.zerobase.reservation.customer.repository.ReservationRepository;
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
}
