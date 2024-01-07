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
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
public class ClientServiceImpl implements ClientService{
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TemplateEngine templateEngine;

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
    public Reservation verify(String verifycode){
        Reservation reservation = reservationRepository.findReservationBylienReseration(verifycode);
        if (reservation == null || reservation.isStatus()){
            return  null;
        }else{
            reservationRepository.Upstatus(reservation.getId());
            return  reservation;
        }
    }

    public static void convertHtmlToPdf(String html, OutputStream outputStream) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(reader);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generateSuccessHtml(Date dateArrive,String prenom, String nom,String CategoryName,Date dateDepart,String adresse,String email,String id,float prix) {
        // Create a Thymeleaf context
        Context thymeleafContext = new Context();
        thymeleafContext.setVariable("message", "Verification successful!");
        thymeleafContext.setVariable("dateArrive", dateArrive);
        thymeleafContext.setVariable("dateDepart", dateDepart);
        thymeleafContext.setVariable("prenom", prenom);
        thymeleafContext.setVariable("adresse", adresse);
        thymeleafContext.setVariable("nom", nom);
        thymeleafContext.setVariable("CategoryName", CategoryName);
        thymeleafContext.setVariable("adresse", adresse);
        thymeleafContext.setVariable("email", email);
        thymeleafContext.setVariable("id", id);
        thymeleafContext.setVariable("prix", prix);
        // Process the Thymeleaf template manually
        return templateEngine.process("successTemplate", thymeleafContext);
    }

    @Override
    public void sendVerificationEmailWithAttachment(String code, byte[] pdfAttachment) throws MessagingException, UnsupportedEncodingException {
        Reservation reservation = reservationRepository.findReservationBylienReseration(code);
        if (reservation != null) {
            String subject = "Verification Successful";
            String senderName = "Hotel Manager";
            String mailContent = "Dear " + reservation.getClient().getNom() + " " + reservation.getClient().getPrenom() + ",\n";
            mailContent += "Your verification was successful! Please find the attached PDF for confirmation.";

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("h3456168@gmail.com", senderName);
            helper.setTo(reservation.getClient().getEmail());
            helper.setSubject(subject);
            helper.setText(mailContent);

            // Attach the PDF to the email
            helper.addAttachment("verification_success.pdf", new ByteArrayResource(pdfAttachment));

            javaMailSender.send(message);
        }
    }


}
