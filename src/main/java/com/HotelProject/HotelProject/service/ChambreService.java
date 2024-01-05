package com.HotelProject.HotelProject.service;

import com.HotelProject.HotelProject.model.Chambre;

import java.util.Date;
import java.util.List;

public interface ChambreService {
    public List<Chambre> getAvailableChambre(Long categorieId, Date dateDepart, Date dateArrive);

    Chambre getById(Long chambreId);
}
