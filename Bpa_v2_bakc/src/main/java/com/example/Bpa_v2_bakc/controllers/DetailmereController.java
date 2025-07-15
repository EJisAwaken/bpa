package com.example.Bpa_v2_bakc.controllers;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bpa_v2_bakc.entities.sql_server.Detailmere;
import com.example.Bpa_v2_bakc.services.DetailmereService;

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
@RequestMapping("/api/detail-mere")
@CrossOrigin("*")
public class DetailmereController {
    @Autowired
    private DetailmereService detailmereService;

    @GetMapping()
    public ResponseEntity<List<Detailmere>> getMethodName(@RequestParam String mere) {
        List<Detailmere> detailmeres = detailmereService.getByMere(mere);
        return ResponseEntity.ok(detailmeres);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateArticle() {
        try {
            detailmereService.importDetailmere();
            return ResponseEntity.status(HttpStatus.OK).body("Article updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update article.");
        }
    }    
    
}
