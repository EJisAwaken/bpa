package com.example.Bpa_v2_bakc.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.Bpa_v2_bakc.entities.sql_server.Facture;
import com.example.Bpa_v2_bakc.services.FactureService;

import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/facture")
@CrossOrigin("*")
public class FactureController {
    @Autowired
    private FactureService factureService;


    @GetMapping()
    public ResponseEntity<Page<Facture>> getMethodName(Pageable page,@RequestParam(defaultValue = "") String critere) {
        Page<Facture> factures = factureService.findAllPage(page, critere);
        return ResponseEntity.ok(factures);
    }

    @GetMapping("/byUid")
    public ResponseEntity<Facture> getMethodName(@RequestParam String uid) {
        Facture facture = factureService.getByUid(uid);
        return ResponseEntity.ok(facture);
    }
    
    @PutMapping("/update")
    public ResponseEntity<String> updateArticle() {
        try {
            factureService.importFacture();
            return ResponseEntity.status(HttpStatus.OK).body("Article updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update article.");
        }
    } 
}
