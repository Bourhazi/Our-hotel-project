package com.HotelProject.HotelProject.service;

import com.HotelProject.HotelProject.model.Reservation;
import com.HotelProject.HotelProject.repository.ReservationRepository;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface ReservationService {
    public Reservation save(Reservation reservation);
}
