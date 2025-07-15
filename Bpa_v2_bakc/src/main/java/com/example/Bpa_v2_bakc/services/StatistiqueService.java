package com.example.Bpa_v2_bakc.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Bpa_v2_bakc.entities.data_dashboard.DataAdmin;
import com.example.Bpa_v2_bakc.entities.data_dashboard.DataAttente;
import com.example.Bpa_v2_bakc.entities.data_dashboard.DataChart;
import com.example.Bpa_v2_bakc.repositories.jdbcTemplate.StatistiqueRepository;

@Service
public class StatistiqueService {
    @Autowired
    private StatistiqueRepository statistiqueRepository;

    public DataAdmin geDataVal(String date1, String date2,int idUser){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        String d1, d2;
        try{
            if (date1 != null && !date1.isEmpty() && date2 != null && !date2.isEmpty()) {
                LocalDate parsedDate2 = LocalDate.parse(date2, formatter);
                d2 = parsedDate2.isAfter(now) ? now.format(formatter) : date2; 
                DataAdmin dataVlaDataVal = statistiqueRepository.getDataForValidator(date1, d2,idUser);
                return dataVlaDataVal;
            } else if ((date1 == null || date1.isEmpty()) && date2 != null && !date2.isEmpty()) {
                LocalDate parsedDate2 = LocalDate.parse(date2, formatter);
                d2 = parsedDate2.isAfter(now) ? now.format(formatter) : date2; 
                d1 = parsedDate2.withDayOfYear(1).format(formatter);
                DataAdmin dataVlaDataVal = statistiqueRepository.getDataForValidator(d1, d2,idUser);
                return dataVlaDataVal;
            } else if (date1 != null && !date1.isEmpty() && (date2 == null || date2.isEmpty())) {
                d2 = now.format(formatter);  
                DataAdmin dataVlaDataVal = statistiqueRepository.getDataForValidator(date1, d2,idUser);
                return dataVlaDataVal;
            } else if ((date1 == null || date1.isEmpty()) && (date2 == null || date2.isEmpty())) {
                d1 = now.withDayOfYear(1).format(formatter);
                d2 = now.format(formatter); 
                DataAdmin dataVlaDataVal = statistiqueRepository.getDataForValidator(d1, d2,idUser);
                return dataVlaDataVal;
            }else{
                DataAdmin defaultData = new DataAdmin();
                defaultData.setValidDA(0);
                defaultData.setRefusDA(0);
                defaultData.setValidBC(0);
                defaultData.setRefusBC(0);
                defaultData.setTotal_valid(0);
                defaultData.setTotal_refus(0);
                defaultData.setTotal(0);

                return defaultData;
            }
        }catch (Exception e) {
            System.err.println("Erreur lors de la récupération des statistiques du dashboard : " + e.getMessage());
            return null;
        }
    }

    public DataAdmin geDataAdmin(String date1, String date2){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        String d1, d2;
        try{
            if (date1 != null && !date1.isEmpty() && date2 != null && !date2.isEmpty()) {
                LocalDate parsedDate2 = LocalDate.parse(date2, formatter);
                d2 = parsedDate2.isAfter(now) ? now.format(formatter) : date2; 
                DataAdmin dataVlaDataAdmin = statistiqueRepository.getDataForAdministrator(date1, d2);
                System.out.println("1 :" + dataVlaDataAdmin.getTotal());
                return dataVlaDataAdmin;
            } else if ((date1 == null || date1.isEmpty()) && date2 != null && !date2.isEmpty()) {
                LocalDate parsedDate2 = LocalDate.parse(date2, formatter);
                d2 = parsedDate2.isAfter(now) ? now.format(formatter) : date2; 
                d1 = parsedDate2.withDayOfYear(1).format(formatter);
                DataAdmin dataVlaDataAdmin = statistiqueRepository.getDataForAdministrator(d1, d2);
                System.out.println("2 :" + dataVlaDataAdmin.getTotal());
                return dataVlaDataAdmin;
            } else if (date1 != null && !date1.isEmpty() && (date2 == null || date2.isEmpty())) {
                d2 = now.format(formatter);  
                DataAdmin dataVlaDataAdmin = statistiqueRepository.getDataForAdministrator(date1, d2);
                System.out.println("3 :" + dataVlaDataAdmin.getTotal());
                return dataVlaDataAdmin;
            } else if ((date1 == null || date1.isEmpty()) && (date2 == null || date2.isEmpty())) {
                d1 = now.withDayOfYear(1).format(formatter);
                d2 = now.format(formatter); 
                DataAdmin dataVlaDataAdmin = statistiqueRepository.getDataForAdministrator(d1, d2);
                System.out.println("4 :" + dataVlaDataAdmin.getTotal());
                return dataVlaDataAdmin;
            }else{
                DataAdmin defaultData = new DataAdmin();
                defaultData.setValidDA(0);
                defaultData.setRefusDA(0);
                defaultData.setValidBC(0);
                defaultData.setRefusBC(0);
                defaultData.setTotal_valid(0);
                defaultData.setTotal_refus(0);
                defaultData.setTotal(0);
                System.out.println("5 tonga de ato be fotsiny");
                return defaultData;
            }
        }catch (Exception e) {
            System.err.println("Erreur lors de la récupération des statistiques du dashboard : " + e.getMessage());
            return null;
        }
    }

    public DataAttente getDataAttenteVal(String idUser){
        try{
            DataAttente dataVlaDataAttente = statistiqueRepository.getAttenteForValidator(idUser);
            return dataVlaDataAttente;
        }catch (Exception e) {
            System.err.println("Erreur lors de la récupération des statistiques du dashboard : " + e.getMessage());
            DataAttente defaultData = new DataAttente();
            defaultData.setTotal_da(0);
            defaultData.setTotal_bc(0);
            defaultData.setTotal_fc(0);
            defaultData.setTotal(0);
            System.out.println("5 tonga de ato be fotsiny");
            return defaultData;
        }
    }

    public DataAttente getDataAttente(){
        try{
            DataAttente dataVlaDataAttente = statistiqueRepository.getAttenteForAdministrator();
            return dataVlaDataAttente;
        }catch (Exception e) {
            System.err.println("Erreur lors de la récupération des statistiques du dashboard : " + e.getMessage());
            DataAttente defaultData = new DataAttente();
            defaultData.setTotal_da(0);
            defaultData.setTotal_bc(0);
            defaultData.setTotal_fc(0);
            defaultData.setTotal(0);
            return defaultData;
        }
    }

    public List<DataChart> getDataChartUser(String date1, String date2,int idUser){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        String d1, d2;
        try{
            if (date1 != null && !date1.isEmpty() && date2 != null && !date2.isEmpty()) {
                LocalDate parsedDate2 = LocalDate.parse(date2, formatter);
                d2 = parsedDate2.isAfter(now) ? now.format(formatter) : date2; 
                return statistiqueRepository.getDataChartUser(date1, d2,idUser);
            } else if ((date1 == null || date1.isEmpty()) && date2 != null && !date2.isEmpty()) {
                LocalDate parsedDate2 = LocalDate.parse(date2, formatter);
                d2 = parsedDate2.isAfter(now) ? now.format(formatter) : date2; 
                d1 = parsedDate2.withDayOfYear(1).format(formatter);
                return statistiqueRepository.getDataChartUser(d1, d2,idUser);
            } else if (date1 != null && !date1.isEmpty() && (date2 == null || date2.isEmpty())) {
                d2 = now.format(formatter); 
                return statistiqueRepository.getDataChartUser(date1, d2,idUser);
            } else if ((date1 == null || date1.isEmpty()) && (date2 == null || date2.isEmpty())) {
                d1 = now.withDayOfYear(1).format(formatter);
                d2 = now.format(formatter); 
                return statistiqueRepository.getDataChartUser(d1, d2,idUser);
            }else{
                return null;
            }
        }catch (Exception e) {
            System.err.println("Erreur lors de la récupération des statistiques du chart : " + e.getMessage());
            return null;
        }
    }

    public List<DataChart> getDataChartAdmin(String date1, String date2){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        String d1, d2;
        try{
            if (date1 != null && !date1.isEmpty() && date2 != null && !date2.isEmpty()) {
                LocalDate parsedDate2 = LocalDate.parse(date2, formatter);
                d2 = parsedDate2.isAfter(now) ? now.format(formatter) : date2; 
                return statistiqueRepository.getDataChartAdmin(date1, d2);
            } else if ((date1 == null || date1.isEmpty()) && date2 != null && !date2.isEmpty()) {
                LocalDate parsedDate2 = LocalDate.parse(date2, formatter);
                d2 = parsedDate2.isAfter(now) ? now.format(formatter) : date2; 
                d1 = parsedDate2.withDayOfYear(1).format(formatter);
                return statistiqueRepository.getDataChartAdmin(d1, d2);
            } else if (date1 != null && !date1.isEmpty() && (date2 == null || date2.isEmpty())) {
                d2 = now.format(formatter); 
                return statistiqueRepository.getDataChartAdmin(date1, d2);
            } else if ((date1 == null || date1.isEmpty()) && (date2 == null || date2.isEmpty())) {
                d1 = now.withDayOfYear(1).format(formatter);
                d2 = now.format(formatter); 
                return statistiqueRepository.getDataChartAdmin(d1, d2);
            }else{
                return null;
            }
        }catch (Exception e) {
            System.err.println("Erreur lors de la récupération des statistiques du chart : " + e.getMessage());
            return null;
        }
    }
}
