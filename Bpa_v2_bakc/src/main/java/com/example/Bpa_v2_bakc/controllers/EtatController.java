package com.example.Bpa_v2_bakc.controllers;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bpa_v2_bakc.entities.mysql.Etat;
import com.example.Bpa_v2_bakc.services.EtatService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/etat")
@CrossOrigin("*")
public class EtatController {
    @Autowired
    private EtatService etatService;


    @GetMapping()
    public ResponseEntity<List<Etat>> getMethodName() {
        List<Etat> etats= etatService.findAll();
        return ResponseEntity.ok(etats);
    }
    
}
