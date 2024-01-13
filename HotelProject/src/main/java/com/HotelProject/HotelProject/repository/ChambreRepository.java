package com.HotelProject.HotelProject.repository;
import com.HotelProject.HotelProject.model.Category;
import com.HotelProject.HotelProject.model.Chambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ChambreRepository extends JpaRepository<Chambre,Long> {

    @Query("SELECT c FROM Chambre c " +
            "WHERE c.category = :category " +
            "AND NOT EXISTS (" +
            "   SELECT r FROM Reservation r " +
            "   WHERE r.chambre = c " +
            "   AND ((r.dateDepart >= :dateArrive AND r.dateArrive <= :dateArrive) " +
            "        OR (r.dateDepart >= :dateDepart AND r.dateArrive <= :dateDepart) " +
            "        OR (r.dateDepart <= :dateDepart AND r.dateArrive >= :dateArrive)))")
    List<Chambre> findAvailableChambresByCategoryAndDates(
            @Param("category") Category category,
            @Param("dateDepart") Date dateDepart,
            @Param("dateArrive") Date dateArrive
    );
}
