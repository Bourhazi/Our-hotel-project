package com.HotelProject.HotelProject.web.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data @AllArgsConstructor @NoArgsConstructor
public class ClientDto {
    private long id;
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private String cin;
    private String gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateDeNaissance;
    private String email;
}
