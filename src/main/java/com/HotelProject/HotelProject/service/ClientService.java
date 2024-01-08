package com.HotelProject.HotelProject.service;

import com.HotelProject.HotelProject.model.Client;
import com.HotelProject.HotelProject.model.Reservation;
import com.HotelProject.HotelProject.web.dto.ClientDto;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.*;
import java.util.Date;

public interface ClientService {
    public Client save(ClientDto ClientDto);

    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public void sendVerificationEmail(ClientDto clientDto ,String lienReservation, String siteUrl) throws MessagingException, UnsupportedEncodingException;

    public Reservation verify(String verifycode);

    public String generateSuccessHtml(Date dateArrive, String prenom, String nom, String CategoryName, Date dateDepart, String adresse, String email, String id, float prix);

    public void sendVerificationEmailWithAttachment(String code, byte[] pdfAttachment) throws MessagingException, UnsupportedEncodingException;

}
