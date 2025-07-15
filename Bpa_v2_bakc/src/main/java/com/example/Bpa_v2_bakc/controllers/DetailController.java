package com.example.Bpa_v2_bakc.controllers;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bpa_v2_bakc.entities.sql_server.Detail;
import com.example.Bpa_v2_bakc.services.DetailService;

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
@RequestMapping("/api/detail")
@CrossOrigin("*")
public class DetailController {
    @Autowired
    private DetailService detailService;


    @GetMapping()
    public ResponseEntity<List<Detail>> findByUid(@RequestParam String uid,@RequestParam String id_x3) {
        List<Detail>  details = detailService.findByUid(uid,id_x3);
        return ResponseEntity.ok(details);
    }

    @PutMapping("/valider")
    public ResponseEntity<String> valider(
        @RequestParam String uid,
        @RequestParam String ref_dem,
        @RequestParam String code_article,
        @RequestParam String id_x3,
        @RequestParam(defaultValue = "") String motif,
        @RequestParam(defaultValue = "3") int valeur1,
        @RequestParam(defaultValue = "3") int valeur2,
        @RequestParam(defaultValue = "5") int valeur3,
        @RequestParam(defaultValue = "0") int idetat,
        @RequestParam(defaultValue = "VAL") String designation
    ) {
        String result = detailService.validationArticle(uid,ref_dem,code_article, id_x3, valeur1, valeur2, valeur3,designation,idetat,motif);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateArticle() {
        try {
            detailService.updateArticle();
            detailService.importDetail();
            return ResponseEntity.status(HttpStatus.OK).body("Article updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update article.");
        }
    }    
}
