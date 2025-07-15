package com.example.Bpa_v2_bakc.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.Bpa_v2_bakc.entities.sql_server.Demande;
import com.example.Bpa_v2_bakc.services.DemandeService;

import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/demande")
@CrossOrigin("*")
public class DemandeController {
    @Autowired
    private DemandeService demandeService;
    
    @GetMapping("/niveau1")
    public ResponseEntity<Page<Demande>> getniveau1(@RequestParam(defaultValue = "") String dir,Pageable page,@RequestParam(defaultValue = "") String critere){
        Page<Demande> demandes = demandeService.findAll(dir,page,critere);
        return ResponseEntity.ok(demandes);
    }
    
    @GetMapping("/lecteur")
    public ResponseEntity<Page<Demande>> getforlecteur(Pageable page,@RequestParam(defaultValue = "") String critere,@RequestParam(defaultValue = "") String validateur,@RequestParam(defaultValue = "") int etat){
        Page<Demande> demandes = demandeService.findAllPage(page,critere,validateur,etat);
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/byUid")
    public ResponseEntity<Demande> getByUid(@RequestParam(defaultValue = "") String uid){
        Demande demande = demandeService.findByUid(uid);
        return ResponseEntity.ok(demande);
    }
    
    @GetMapping("/byUidX3")
    public ResponseEntity<Demande> findByUidAndX3(@RequestParam(defaultValue = "") String uid,@RequestParam(defaultValue = "") String id_x3){
        Demande demande = demandeService.findByUidAndX3(uid,id_x3);
        return ResponseEntity.ok(demande);
    }

    @PutMapping("/valider")
    public ResponseEntity<String> valider(
        @RequestParam String uid,
        @RequestParam String id_x3,
        @RequestParam(defaultValue = "") String motif,
        @RequestParam(defaultValue = "3") int valeur1,
        @RequestParam(defaultValue = "3") int valeur2,
        @RequestParam(defaultValue = "5") int valeur3,
        @RequestParam(defaultValue = "0") int idetat,
        @RequestParam(defaultValue = "VAL") String designation
    ) {
        String result = demandeService.validerRequisition(uid, id_x3, valeur1, valeur2, valeur3,designation,idetat,motif);
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/update")
    public ResponseEntity<String> updateRequisition() {
        try {
            demandeService.updateRequisition();
            demandeService.importDemande();
            return ResponseEntity.status(HttpStatus.OK).body("Requisition updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update requisition.");
        }
    }
}
