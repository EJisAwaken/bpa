package com.example.Bpa_v2_bakc.controllers;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bpa_v2_bakc.entities.sql_server.Detailbc;
import com.example.Bpa_v2_bakc.services.DetailbcService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/detailbc")
@CrossOrigin("*")
public class DetailbcController {
    @Autowired
    private DetailbcService detailbcService;

    @GetMapping()
    public ResponseEntity<List<Detailbc>> getAll(@RequestParam String uid) {
        List<Detailbc> detailbcs = detailbcService.findByRefDemuid(uid);
        return ResponseEntity.ok(detailbcs);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateArticle() {
        try {
            detailbcService.importDetailBc();
            return ResponseEntity.status(HttpStatus.OK).body("Article updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update article.");
        }
    }   
    
}
