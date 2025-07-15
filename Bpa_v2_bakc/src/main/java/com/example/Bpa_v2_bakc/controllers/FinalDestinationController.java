package com.example.Bpa_v2_bakc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bpa_v2_bakc.entities.mysql.Finaldestination;
import com.example.Bpa_v2_bakc.services.FinalDesignationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;





@RestController
@RequestMapping("/api/final")
@CrossOrigin("*")
public class FinalDestinationController {
    @Autowired
    private FinalDesignationService finalDesignationService;

    @GetMapping()
    public ResponseEntity<List<Finaldestination>> getAll() {
        List<Finaldestination> finaldestinations = finalDesignationService.getAllFinaldestinations();
        return ResponseEntity.ok(finaldestinations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Finaldestination> getById(@PathVariable int id) {
        Finaldestination finaldestination = finalDesignationService.getByid(id);
        return ResponseEntity.ok(finaldestination);
    }
    

    @PostMapping()
    public void insert(@RequestBody Finaldestination finaldestination) {
        finalDesignationService.insertFinalDestination(finaldestination);
    }

    @PutMapping()
    public void update(@RequestBody Finaldestination entity) {
        finalDesignationService.updateFinalDestination(entity);
    }
    
    @PutMapping("/{id}")
    public void delete(@PathVariable int id) {
        finalDesignationService.deleteFinalDestination(id);
    }
}
