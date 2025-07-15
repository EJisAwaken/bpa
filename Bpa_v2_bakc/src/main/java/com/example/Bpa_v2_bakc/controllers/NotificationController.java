package com.example.Bpa_v2_bakc.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bpa_v2_bakc.entities.mysql.Notification;
import com.example.Bpa_v2_bakc.services.NotificationService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/notification")
@CrossOrigin("*")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping()
    public ResponseEntity<List<Notification>> getMethodName(@RequestParam(required = true) String id_x3) {
        List<Notification> notifs = notificationService.findById_x3(id_x3);
        return ResponseEntity.ok(notifs);
    }

    @PutMapping("/ouvrir")
    public void ouvrirNotif(@RequestParam(defaultValue = "") String uid,@RequestParam(defaultValue = "") String dir) {
        notificationService.ouvrirNotif(uid, dir);
    }

    @PutMapping("/putZero/{dir}")
    public void CountByZero(@PathVariable String dir) {
        notificationService.countByZero(dir);
    }
    
}
