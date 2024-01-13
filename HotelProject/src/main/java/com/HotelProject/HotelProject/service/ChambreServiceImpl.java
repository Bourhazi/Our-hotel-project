package com.HotelProject.HotelProject.service;

import com.HotelProject.HotelProject.model.Category;
import com.HotelProject.HotelProject.model.Chambre;
import com.HotelProject.HotelProject.repository.CategoryRepository;
import com.HotelProject.HotelProject.repository.ChambreRepository;
import com.HotelProject.HotelProject.repository.ReservationRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChambreServiceImpl implements ChambreService {
    @Autowired
    CategoryRepository categoryRepository ;
    @Autowired
    ChambreRepository chambreRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Override
    public List<Chambre> getAvailableChambre(Long categorieId, Date dateDepart, Date dateArrive) {
        Optional<Category> category = categoryRepository.findById(categorieId);
        return chambreRepository.findAvailableChambresByCategoryAndDates(category.get(),dateDepart,dateArrive);
    }
    @Override
    public Chambre getById(Long chambreId) {
        Optional<Chambre> optionalChambre = chambreRepository.findById(chambreId);
        return optionalChambre.orElse(null);
    }
}
