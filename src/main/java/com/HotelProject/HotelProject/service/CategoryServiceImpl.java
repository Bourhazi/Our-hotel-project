package com.HotelProject.HotelProject.service;

import com.HotelProject.HotelProject.model.Category;
import com.HotelProject.HotelProject.model.Reservation;
import com.HotelProject.HotelProject.repository.CategoryRepository;
import com.HotelProject.HotelProject.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository ;
    @Autowired
    private ReservationRepository reservationRepository;
    @Override
    public List<Category> ListCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }
    @Override
    public boolean checkAvailability(Long categorieId, Date dateDepart, Date dateArrive) {
        List<Reservation> reservations = reservationRepository.findReservationsByCategoryAndDateRange(categorieId, dateDepart, dateArrive);
        if (reservations.isEmpty()) {
            System.out.println("Aucune réservation trouvée.");
        } else {
            System.out.println("Liste des réservations :");
            for (Reservation reservation : reservations) {
                System.out.println("ID de la réservation : " + reservation.getId());
                System.out.println("Date de départ : " + reservation.getDateDepart());
                System.out.println("Date d'arrivée : " + reservation.getDateArrive());
            }
        }
        return reservations.isEmpty();
    }
}
