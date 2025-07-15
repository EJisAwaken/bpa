package com.example.Bpa_v2_bakc.controllers;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bpa_v2_bakc.entities.mysql.Validation;
import com.example.Bpa_v2_bakc.services.ValidationService;

import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/validation")
@CrossOrigin("*")
public class ValidationController {
    @Autowired
    private ValidationService validationService;

    @GetMapping("/byUser")
    public ResponseEntity<Page<Validation>> getByUser(@RequestParam(defaultValue = "0") int page,@RequestParam int idUser,@RequestParam String date1,@RequestParam String date2) {
        Page<Validation> validations = validationService.getByUser(page,date1,date2,idUser);
        return ResponseEntity.ok(validations);
    }
    
    @GetMapping("/all")
    public ResponseEntity<Page<Validation>> getAll(@RequestParam(defaultValue = "0") int page,@RequestParam String date1,@RequestParam String date2,@RequestParam(required = false) int idUser) {
        Page<Validation> validations = validationService.getAll(page,date1,date2,idUser);
        return ResponseEntity.ok(validations);
    }
    
    @GetMapping()
    public ResponseEntity<Validation> getValidationSelected(@RequestParam String uid,@RequestParam(defaultValue = "") int idUser) {
        Validation validation = validationService.getValidationSelected(uid,idUser);
        return ResponseEntity.ok(validation);
    }
}
