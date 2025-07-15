package com.example.Bpa_v2_bakc.controllers;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bpa_v2_bakc.entities.sql_server.Facturecompett;
import com.example.Bpa_v2_bakc.services.FacturecompettService;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/facture-comp-ett")
@CrossOrigin("*")
public class FacturecompettController {
    @Autowired
    private FacturecompettService facturecompettService;

    @GetMapping()
    public ResponseEntity<Page<Facturecompett>> getMethodName(Pageable page,@RequestParam(defaultValue = "") String critere) {
        Page<Facturecompett> factures = facturecompettService.findAllPage(page, critere);
        return ResponseEntity.ok(factures);
    }

    @GetMapping("/lecteur")
    public ResponseEntity<Page<Facturecompett>> getMethodName(Pageable page,@RequestParam(defaultValue = "") String critere,@RequestParam(defaultValue = "") int etat) {
        Page<Facturecompett> factures = facturecompettService.findAllForLecPage(page, critere,etat);
        return ResponseEntity.ok(factures);
    }

    @GetMapping("/byUid")
    public ResponseEntity<Facturecompett> getByUid(@RequestParam String uid) {
        Facturecompett facture = facturecompettService.getByUid(uid);
        return ResponseEntity.ok(facture);
    }

    @PutMapping("/valider")
    public ResponseEntity<String> valider(
        @RequestParam String uid,
        @RequestParam String id_x3,
        @RequestParam(defaultValue = "") String motif,
        @RequestParam(defaultValue = "3") int valeur1,
        @RequestParam(defaultValue = "0") int idetat
    ) {
        String result = facturecompettService.validation(uid,id_x3, idetat, valeur1, motif);
        return ResponseEntity.ok(result);
    }
}
