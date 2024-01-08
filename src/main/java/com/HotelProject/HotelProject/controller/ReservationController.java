package com.HotelProject.HotelProject.controller;

import com.HotelProject.HotelProject.model.Chambre;
import com.HotelProject.HotelProject.service.CategoryService;
import com.HotelProject.HotelProject.service.ChambreService;
import com.HotelProject.HotelProject.service.ClientService;
import com.HotelProject.HotelProject.web.dto.ClientDto;
import com.HotelProject.HotelProject.web.dto.ReservationDto;
import com.HotelProject.HotelProject.web.dto.UserRegestrationDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ChambreService chambreService;


    @GetMapping
    public String ReservationClient(Model model){
        model.addAttribute("listeCategories",categoryService.ListCategory());
        return "AjouterReservation";
    }

    @PostMapping("/submitReservation")

    public String submitReservation(@RequestParam("dateDepart") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDepart,
                                    @RequestParam("dateArrive") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateArrive,
                                    @RequestParam("categorieId") Long categorieId,
                                    HttpSession session) {
        if (dateDepart != null && dateArrive != null && dateArrive.before(dateDepart)) {
            List<Chambre> chambres = chambreService.getAvailableChambre(categorieId, dateDepart, dateArrive);
            //System.out.println(chambres.size());
            if (chambres.isEmpty()) {
                return "redirect:/reservation?echec1";
            } else {
                Long chambreId = chambres.get(0).getId();
                Map<String, Object> reservationDetails = new HashMap<>();
                reservationDetails.put("dateDepart", dateDepart);
                reservationDetails.put("dateArrive", dateArrive);
                reservationDetails.put("categorieId", categorieId);
                reservationDetails.put("chambreId",chambreId);
                session.setAttribute("reservationDetails", reservationDetails);
                //System.out.println(session.toString());
                return "redirect:/client";
            }
        } else {
            return "redirect:/reservation?echec";
        }
    }
}
