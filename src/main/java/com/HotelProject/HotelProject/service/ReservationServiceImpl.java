package com.HotelProject.HotelProject.service;

import com.HotelProject.HotelProject.model.Reservation;
import com.HotelProject.HotelProject.repository.ReservationRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService{
    @Autowired
    private ReservationRepository reservationRepository;
    private Reservation reservation;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    @Override
    public Reservation save(Reservation reservation) {
        String randomString = RandomStringUtils.randomAlphanumeric(64);
        reservation.setLienReseration(randomString);
        return reservationRepository.save(reservation);
    }

}
