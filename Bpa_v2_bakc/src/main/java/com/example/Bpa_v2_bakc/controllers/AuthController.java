package com.example.Bpa_v2_bakc.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bpa_v2_bakc.entities.model.IsLogged;
import com.example.Bpa_v2_bakc.entities.object_request.UserRequest;
import com.example.Bpa_v2_bakc.services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest user) {
        IsLogged isLogged = authService.authenticate(user.getEmail(), user.getMot_de_passe());
        if(isLogged != null){
            return ResponseEntity.ok(isLogged);
        }else {
            return ResponseEntity.status(401).build();
        }
    }
}
