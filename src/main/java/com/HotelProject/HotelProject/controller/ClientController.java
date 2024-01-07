package com.HotelProject.HotelProject.controller;


import com.HotelProject.HotelProject.model.Chambre;
import com.HotelProject.HotelProject.model.Client;
import com.HotelProject.HotelProject.model.Reservation;
import com.HotelProject.HotelProject.repository.ChambreRepository;
import com.HotelProject.HotelProject.service.ChambreService;
import com.HotelProject.HotelProject.service.ClientService;
import com.HotelProject.HotelProject.service.ClientServiceImpl;
import com.HotelProject.HotelProject.service.ReservationService;
import com.HotelProject.HotelProject.web.dto.ClientDto;
import com.HotelProject.HotelProject.web.dto.UserRegestrationDto;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.eclipse.angus.mail.imap.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private ChambreService chambreService;

    @Autowired
    private ReservationService reservationService;

    //private ChambreRepository chambreRepository;

    @ModelAttribute("client")
    public ClientDto clientDto(){
        return new ClientDto();
    }
    @GetMapping
    public String showClientPage(HttpSession session, Model model) {
        Map<String, Object> reservationDetails = (Map<String, Object>) session.getAttribute("reservationDetails");
        if (reservationDetails != null) {
            Date dateDepart = (Date) reservationDetails.get("dateDepart");
            Date dateArrive = (Date) reservationDetails.get("dateArrive");
            Long categorieId = (Long) reservationDetails.get("categorieId");
            Long chambreId = (Long) reservationDetails.get("chambreId");

            model.addAttribute("dateDepart", dateDepart);
            model.addAttribute("dateArrive", dateArrive);
            model.addAttribute("categorieId", categorieId);
            model.addAttribute("chambreId", chambreId);
        }
        return "Client";
    }


    @PostMapping
    public String AddClient(@ModelAttribute("client") ClientDto clientDto, HttpSession session, Model model, HttpServletRequest request)
            throws MessagingException, UnsupportedEncodingException {

        Map<String, Object> reservationDetails = (Map<String, Object>) session.getAttribute("reservationDetails");
        if (reservationDetails != null) {
            Date dateDepart = (Date) reservationDetails.get("dateDepart");
            Date dateArrive = (Date) reservationDetails.get("dateArrive");
            Long categorieId = (Long) reservationDetails.get("categorieId");
            Long chambreId = (Long) reservationDetails.get("chambreId");

            if (dateDepart != null && dateArrive != null && dateArrive.before(dateDepart)) {
                List<Chambre> chambres = chambreService.getAvailableChambre(categorieId, dateDepart, dateArrive);
                if (chambres.isEmpty()) {
                    return "redirect:/reservation";
                } else {
                    try {
                        Client savedClient = clientService.save(clientDto);
                        String siteUrl = ClientService.getSiteURL(request);
                        Reservation reservation = new Reservation();
                        reservation.setDateDepart(dateDepart);
                        reservation.setDateArrive(dateArrive);
                        reservation.setChambre(chambreService.getById(chambreId));
                        reservation.setClient(savedClient);
                        reservationService.save(reservation);
                        clientService.sendVerificationEmail(clientDto,reservation.getLienReseration(),siteUrl);
                        return  "redirect:/client?success";
                    } catch (DataIntegrityViolationException e) {
                        //model.addAttribute("error", "Le client avec ce CIN existe déjà.");
                        //return "votre_page_d'erreur";
                    }
                }
            } else {
                return "redirect:/reservation?error=invalidDates";
            }
        }
        return "redirect:/reservation";
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code, Model model, HttpServletResponse response) throws IOException {
        boolean redirectSuccess = false;
        try {
            Reservation reservation = clientService.verify(code);
            if (reservation != null) {
                Date dateArrive = reservation.getDateArrive();
                String prenom = reservation.getClient().getPrenom();
                String nom = reservation.getClient().getNom();
                String CategoryName = reservation.getChambre().getCategory().getName();
                Date dateDepart = reservation.getDateDepart();
                String adresse = reservation.getClient().getAdresse();
                String email = reservation.getClient().getEmail();
                Long id= reservation.getChambre().getId();
                float prix= reservation.getChambre().getPrix();

                String successHtml = clientService.generateSuccessHtml(dateArrive,prenom,nom,CategoryName,dateDepart,adresse,email, String.valueOf(id),prix);
                ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
                ClientServiceImpl.convertHtmlToPdf(successHtml, pdfOutputStream);
                byte[] pdfBytes = pdfOutputStream.toByteArray();
                clientService.sendVerificationEmailWithAttachment(code, pdfBytes);
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=verification_success.pdf");
                response.getOutputStream().write(pdfBytes);
                redirectSuccess = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (redirectSuccess) {
            return "redirect:/reservation?success1";
        } else {
            return "redirect:/reservation/echec2";
        }
    }




}
