package com.example.Bpa_v2_bakc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Bpa_v2_bakc.entities.mysql.Etat;
import com.example.Bpa_v2_bakc.repositories.mysql.EtatRepository;

@Service
public class EtatService {
    @Autowired
    private EtatRepository etatRepository;


    public Etat findById(int id_etat){
        Etat etat = etatRepository.findByIdEtat(id_etat)
            .orElseThrow(() -> new RuntimeException("Etat non trouvé"));
        return etat;
    }

    public List<Etat> findAll(){
        return etatRepository.findAll();
    }
}
