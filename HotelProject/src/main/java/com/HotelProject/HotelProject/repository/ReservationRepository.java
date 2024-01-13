package com.HotelProject.HotelProject.repository;

import com.HotelProject.HotelProject.model.Client;
import com.HotelProject.HotelProject.model.Reservation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.chambre c " +
            "JOIN FETCH c.category cat " +
            "WHERE cat.id = :categoryId " +
            "AND ((r.dateArrive <:startDate) AND (r.dateDepart > :endDate))")
    List<Reservation> findReservationsByCategoryAndDateRange(
            @Param("categoryId") Long categoryId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
    @Transactional
    @Query("UPDATE Reservation r SET r.status=true WHERE r.id = ?1")
    @Modifying
    public void Upstatus(long id);
    @Query("Select r FROM Reservation r WHERE r.lienReseration = ?1")
    public Reservation findReservationBylienReseration(String code);

    @Query("SELECT r FROM Reservation r WHERE r.status = false AND r.dateCreated < :expirationDate")
    List<Reservation> findExpiredReservations(@Param("expirationDate") Date expirationDate);
}
