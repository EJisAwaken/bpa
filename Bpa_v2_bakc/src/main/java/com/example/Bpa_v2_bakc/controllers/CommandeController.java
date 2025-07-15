package com.example.Bpa_v2_bakc.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.Bpa_v2_bakc.entities.sql_server.Commande;
import com.example.Bpa_v2_bakc.services.CommandeService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
@RequestMapping("/api/commande")
@CrossOrigin("*")
public class CommandeController {
    @Autowired
    private CommandeService commandeService;

    @GetMapping()
    public ResponseEntity<Page<Commande>> findniveau1(@RequestParam(defaultValue = "") String dir,Pageable page,@RequestParam(defaultValue = "") String critere) {
        Page<Commande> commandes = commandeService.findAll(dir, page, critere);
        return ResponseEntity.ok(commandes);
    }
    
    @GetMapping("/lecteur")
    public ResponseEntity<Page<Commande>> findForLecteur(Pageable page,@RequestParam(defaultValue = "") String critere,@RequestParam(defaultValue = "") String validateur,@RequestParam(defaultValue = "") int etat) {
        Page<Commande> commandes = commandeService.findForLecteur(page, critere,validateur,etat);
        return ResponseEntity.ok(commandes);
    }

    @GetMapping("/lecteur-detail")
    public ResponseEntity<Commande> findForLecteur(@RequestParam(defaultValue = "") String uid,@RequestParam(defaultValue = "") String id_x3) {
        Commande commande = commandeService.findByUidIdX3(uid,id_x3);
        return ResponseEntity.ok(commande);
    }
    
    @GetMapping("/byUid")
    public ResponseEntity<List<Commande>> findByUid(@RequestParam(defaultValue = "") String uid,@RequestParam(defaultValue = "") String id_x3) {
        List<Commande> commandes = commandeService.findByUidAndIdx3(uid,id_x3);
        return ResponseEntity.ok(commandes);
    }
    
    @PutMapping("/valider")
    public ResponseEntity<String> valider(
        @RequestParam String uid,
        @RequestParam String id_x3,
        @RequestParam(defaultValue = "") String motif,
        @RequestParam(defaultValue = "3") int valeur1,
        @RequestParam(defaultValue = "0") int idetat,
        @RequestParam(defaultValue = "VAL") String designation
    ) {
        String result = commandeService.validerCommande(uid, id_x3, valeur1,designation,idetat,motif);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCommande() {
        try {
            commandeService.updateCommande();
            commandeService.importCommande();
            return ResponseEntity.status(HttpStatus.OK).body("Requisition updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update requisition.");
        }
    }
}
