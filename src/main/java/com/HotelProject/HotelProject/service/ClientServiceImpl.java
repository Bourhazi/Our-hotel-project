package com.HotelProject.HotelProject.service;

import com.HotelProject.HotelProject.model.Client;
import com.HotelProject.HotelProject.model.Reservation;
import com.HotelProject.HotelProject.model.Role;
import com.HotelProject.HotelProject.model.User;
import com.HotelProject.HotelProject.repository.ClientRepository;
import com.HotelProject.HotelProject.repository.ReservationRepository;
import com.HotelProject.HotelProject.web.dto.ClientDto;
import com.HotelProject.HotelProject.web.dto.UserRegestrationDto;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

@Service
public class ClientServiceImpl implements ClientService{
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ReservationRepository reservationRepository;

    //public static String getSiteURL(HttpServletRequest request){

    //}
    @Override
    public Client save(ClientDto ClientDto) {
        Client client = new Client(ClientDto.getNom(),ClientDto.getPrenom(),ClientDto.getAdresse(),ClientDto.getTelephone(),
                ClientDto.getCin(),ClientDto.getGender(),ClientDto.getDateDeNaissance(),ClientDto.getEmail());
        return clientRepository.save(client);
    }

    @Override
    public void sendVerificationEmail(ClientDto clientDto ,String lienReservation , String siteUrl) throws MessagingException, UnsupportedEncodingException {
        String subject="Please verify you inscription";
        String senderName="Hotel Manager";
        String mailContent = "<p>Dear "+clientDto.getNom()+" "+clientDto.getPrenom()+", </p>";
        String verifyURL = siteUrl + "/client/verify?code="+lienReservation;
        mailContent+="<h3><a href=\""+verifyURL+"\"> VERIFY </a></h3>";
        mailContent+="<p>Please Click the link below to verify to your inscription :</p>";
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("h3456168@gmail.com", senderName);
        helper.setTo(clientDto.getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent,true);
        javaMailSender.send(message);
    }
    @Override
    public boolean verify(String verifycode){
        Reservation reservation = reservationRepository.findReservationBylienReseration(verifycode);
        if (reservation == null || reservation.isStatus()){
            return  false;
        }else{
            reservationRepository.Upstatus(reservation.getId());
            return  true;
        }
    }
    @Override
    public void export(String outputPdfPath) throws Exception {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html>");
        htmlContent.append("<html>");
        htmlContent.append("<head>");
        htmlContent.append("<title>Reservation Details</title>");
        htmlContent.append("</head>");
        htmlContent.append("<body>");
        htmlContent.append("<h1>Reservation Details</h1>");
        htmlContent.append("<p>Test Test</p>");
        htmlContent.append("</body>");
        htmlContent.append("</html>");

        try (OutputStream os = new FileOutputStream(outputPdfPath)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent.toString());
            renderer.layout();
            renderer.createPDF(os);
            renderer.finishPDF();
        }
    }
}
