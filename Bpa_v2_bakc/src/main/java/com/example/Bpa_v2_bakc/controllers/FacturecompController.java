package com.example.Bpa_v2_bakc.controllers;
import org.springframework.web.bind.annotation.RestController;
import com.example.Bpa_v2_bakc.entities.sql_server.Facturecomp;
import com.example.Bpa_v2_bakc.services.FacturecompService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/facture-comp")
@CrossOrigin("*")
public class FacturecompController {
    @Autowired
    private FacturecompService facturecompService; 

    @GetMapping()
    public ResponseEntity<List<Facturecomp>> getMethodName(@RequestParam String mere) {
        List<Facturecomp> facturecomps = facturecompService.getByMere(mere);
        return ResponseEntity.ok(facturecomps);
    }

    @GetMapping("/byUid")
    public ResponseEntity<List<Facturecomp>> getByUid(@RequestParam String uid) {
        List<Facturecomp> facture = facturecompService.getByUid(uid);
        return ResponseEntity.ok(facture);
    }
    
    // @GetMapping("/byUid")
    // public ResponseEntity<Facturecomp> getByUid(@RequestParam String uid) {
    //     Facturecomp facture = facturecompService.getByUid(uid);
    //     return ResponseEntity.ok(facture);
    // }

    // @PutMapping("/valider")
    // public ResponseEntity<String> valider(
    //     @RequestParam String uid,
    //     @RequestParam String mere,
    //     @RequestParam String id_x3,
    //     @RequestParam(defaultValue = "") String motif,
    //     @RequestParam(defaultValue = "3") int valeur1,
    //     @RequestParam(defaultValue = "0") int idetat
    // ) {
    //     String result = facturecompService.validation_facture(uid,mere, id_x3, idetat, valeur1, motif);
    //     return ResponseEntity.ok(result);
    // }
    
    @PutMapping("/update")
    public ResponseEntity<String> updateArticle() {
        try {
            facturecompService.importFacturecomp();
            return ResponseEntity.status(HttpStatus.OK).body("Article updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update article.");
        }
    } 
}
