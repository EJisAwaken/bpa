package com.example.Bpa_v2_bakc.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.Bpa_v2_bakc.entities.mysql.Etat;
import com.example.Bpa_v2_bakc.entities.mysql.User;
import com.example.Bpa_v2_bakc.entities.mysql.Validation;
import com.example.Bpa_v2_bakc.repositories.mysql.ValidationRepository;

@Service
public class ValidationService {
    @Autowired
    private ValidationRepository validationRepository;

    @Autowired
    private UserService userService;


    public void saveValidation(String demande,String objet,String montant,String motif, Etat etat ,User user,int is_da){
        Validation validation = new Validation();
        validation.setUser(user);
        validation.setEtat(etat);
        validation.setDemande(demande);
        validation.setObjet(objet);
        validation.setMontant(montant);
        validation.setMotif(motif);
        validation.setIs_da(is_da);
        validation.setDate_validation(LocalDateTime.now());
        validationRepository.save(validation);
    }

    public Validation getValidationSelected(String uid, int idUser){
        User user = userService.getUserById(idUser);
        Optional<Validation> validation= validationRepository.findByDemandeAndUser(uid, user);
        if (validation.isPresent()) {
            return validation.get();
        }else{
            System.out.println("Validation introuvable");
            return null;
        }
    }

    public Page<Validation> getAll(int page,String date1, String date2,int idUser){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        String d1, d2;
        try{
            PageRequest pageable = PageRequest.of(page, 15);
            if (date1 != null && !date1.isEmpty() && date2 != null && !date2.isEmpty()) {
                LocalDate parsedDate2 = LocalDate.parse(date2, formatter);
                d2 = parsedDate2.isAfter(now) ? now.format(formatter) : date2; 
                Page<Validation> dataUser = validationRepository.findAllValidation(pageable,date1, d2,idUser);
                return dataUser;
            } else if ((date1 == null || date1.isEmpty()) && date2 != null && !date2.isEmpty()) {
                LocalDate parsedDate2 = LocalDate.parse(date2, formatter);
                d2 = parsedDate2.isAfter(now) ? now.format(formatter) : date2; 
                d1 = parsedDate2.withDayOfYear(1).format(formatter);
                Page<Validation> dataUser = validationRepository.findAllValidation(pageable,d1, d2,idUser);
                return dataUser;
            } else if (date1 != null && !date1.isEmpty() && (date2 == null || date2.isEmpty())) {
                d2 = now.format(formatter);  
                Page<Validation> dataUser = validationRepository.findAllValidation(pageable,date1, d2,idUser);
                return dataUser;
            } else if ((date1 == null || date1.isEmpty()) && (date2 == null || date2.isEmpty())) {
                d1 = now.withDayOfYear(1).format(formatter);
                d2 = now.format(formatter); 
                Page<Validation> dataUser = validationRepository.findAllValidation(pageable,d1, d2,idUser);
                return dataUser;
            }else{
                return null;
            }
        }catch (Exception e) {
            System.err.println("Erreur lors de la récupération des statistiques du dashboard : " + e.getMessage());
            return null;
        }
    }
    
    public Page<Validation> getByUser(int page,String date1, String date2,int idUser){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        String d1, d2;
        try{
            PageRequest pageable = PageRequest.of(page, 15);
            if (date1 != null && !date1.isEmpty() && date2 != null && !date2.isEmpty()) {
                LocalDate parsedDate2 = LocalDate.parse(date2, formatter);
                d2 = parsedDate2.isAfter(now) ? now.format(formatter) : date2; 
                Page<Validation> dataUser = validationRepository.findByIdUser(idUser,pageable,date1, d2);
                return dataUser;
            } else if ((date1 == null || date1.isEmpty()) && date2 != null && !date2.isEmpty()) {
                LocalDate parsedDate2 = LocalDate.parse(date2, formatter);
                d2 = parsedDate2.isAfter(now) ? now.format(formatter) : date2; 
                d1 = parsedDate2.withDayOfYear(1).format(formatter);
                Page<Validation> dataUser = validationRepository.findByIdUser(idUser,pageable,d1, d2);
                return dataUser;
            } else if (date1 != null && !date1.isEmpty() && (date2 == null || date2.isEmpty())) {
                d2 = now.format(formatter);  
                Page<Validation> dataUser = validationRepository.findByIdUser(idUser,pageable,date1, d2);
                return dataUser;
            } else if ((date1 == null || date1.isEmpty()) && (date2 == null || date2.isEmpty())) {
                d1 = now.withDayOfYear(1).format(formatter);
                d2 = now.format(formatter); 
                Page<Validation> dataUser = validationRepository.findByIdUser(idUser,pageable,d1, d2);
                return dataUser;
            }else{
                return null;
            }
        }catch (Exception e) {
            System.err.println("Erreur lors de la récupération des statistiques du dashboard : " + e.getMessage());
            return null;
        }
    }
}
