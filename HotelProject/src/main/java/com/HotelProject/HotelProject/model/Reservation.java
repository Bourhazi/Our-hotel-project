package com.HotelProject.HotelProject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reservation")
@Data @NoArgsConstructor @AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "date_depart")
    private Date dateDepart;
    @Column(name = "date_arrive")
    private Date dateArrive;
    private boolean status=false;
    @Column(name = "lien_reservation")
    private String lienReseration;
    @CreationTimestamp
    @Column(name = "date_created", updatable = false)
    private Date dateCreated;
    @ManyToOne
    private Chambre chambre;
    @ManyToOne
    private Client client;

}
