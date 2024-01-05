package com.HotelProject.HotelProject.service;

import com.HotelProject.HotelProject.model.Client;
import com.HotelProject.HotelProject.model.User;
import com.HotelProject.HotelProject.web.dto.ClientDto;
import com.HotelProject.HotelProject.web.dto.UserRegestrationDto;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface ClientService {
    public Client save(ClientDto ClientDto);

    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public void sendVerificationEmail(ClientDto clientDto ,String lienReservation, String siteUrl) throws MessagingException, UnsupportedEncodingException;

    public boolean verify(String verifycode);

    public void export(String outputPdfPath) throws Exception ;
}
