package com.example.Bpa_v2_bakc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

import com.example.Bpa_v2_bakc.entities.data_dashboard.DataAdmin;
import com.example.Bpa_v2_bakc.entities.data_dashboard.DataAttente;
import com.example.Bpa_v2_bakc.entities.data_dashboard.DataChart;
import com.example.Bpa_v2_bakc.services.StatistiqueService;

import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/stat")
@CrossOrigin("*")
public class StatistiqueController {
    @Autowired
    private StatistiqueService statistiqueService;


    @GetMapping("/user")
    public ResponseEntity<DataAdmin> getDataVal(@RequestParam String date1,@RequestParam String date2,@RequestParam int idUser){
        DataAdmin dataDashboard = statistiqueService.geDataVal(date1, date2,idUser);
        return ResponseEntity.ok(dataDashboard);
    }
    
    @GetMapping("/admin")
    public ResponseEntity<DataAdmin> getDataAdmin(@RequestParam String date1,@RequestParam String date2){
        DataAdmin dataDashboard = statistiqueService.geDataAdmin(date1, date2);
        return ResponseEntity.ok(dataDashboard);
    }

    @GetMapping("/admin-attente")
    public ResponseEntity<DataAttente> getDataAttente(){
        DataAttente dataDashboard = statistiqueService.getDataAttente();
        return ResponseEntity.ok(dataDashboard);
    }
    
    @GetMapping("/val-attente")
    public ResponseEntity<DataAttente> getDataAttenteVal(@RequestParam String idUser){
        DataAttente dataDashboard = statistiqueService.getDataAttenteVal(idUser);
        return ResponseEntity.ok(dataDashboard);
    }

    @GetMapping("/chartuser")
    public ResponseEntity<List<DataChart>> getDataChartUser(@RequestParam String date1,@RequestParam String date2,@RequestParam int idUser){
        List<DataChart> dataCharts = statistiqueService.getDataChartUser(date1, date2,idUser);
        return ResponseEntity.ok(dataCharts);
    }
    
    @GetMapping("/chartadmin")
    public ResponseEntity<List<DataChart>> getDataChartAdmin(@RequestParam String date1,@RequestParam String date2){
        List<DataChart> dataCharts = statistiqueService.getDataChartAdmin(date1, date2);
        return ResponseEntity.ok(dataCharts);
    }
    
}
