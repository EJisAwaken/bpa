package com.example.Bpa_v2_bakc.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Bpa_v2_bakc.entities.mysql.Etat;
import com.example.Bpa_v2_bakc.entities.sql_server.Facture;
import com.example.Bpa_v2_bakc.repositories.mysql.EtatRepository;
import com.example.Bpa_v2_bakc.repositories.sql_server.FactureRepository;

import jakarta.transaction.Transactional;



@Service
public class FactureService{
    private final FactureRepository factureRepository;
    private final EtatRepository etatRepository;
    private final RestTemplate restTemplate;
    private final String pythonApiUrl;

    public FactureService(
        @Value("${python.api.url}") String pythonApiUrl,
        RestTemplate restTemplate,
        FactureRepository factureRepository,
        EtatRepository etatRepository
    ){
        this.pythonApiUrl = pythonApiUrl;
        this.restTemplate = restTemplate;
        this.factureRepository = factureRepository;
        this.etatRepository = etatRepository;
    }

    public Page<Facture> findAllPage(Pageable pageable, String critere){
        return factureRepository.findAllPage(pageable, critere);
    }

    public Facture getByUid(String uid){
        Optional<Facture> fOptional = factureRepository.findByUid(uid);
        if(fOptional.isPresent()){
            return fOptional.get();
        }else{
            return null;
        }
    }

    @Scheduled(fixedRate = 60000) // Exécution toutes les 5 minutes (300000 ms = 5 min)
    @Transactional
    public void importFacture(){
        String url = pythonApiUrl + "/factures/";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Optional<Etat> etat = etatRepository.findById(6);
        if (response != null && "success".equals(response.get("status"))) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
            data.forEach(item -> {
                String uid = (String) item.get("uid");
                if(etat.isPresent() && !factureRepository.existsByUid(uid)){
                    Facture facture = mapToFacture(item);
                    factureRepository.save(facture);
                }
            });
        }else {
            throw new RuntimeException("Erreur lors de la récupération des données");
        }
    }

    private Facture mapToFacture(Map<String, Object> item){
        Facture facture = new Facture();
        Optional.ofNullable((String) item.get("uid")).ifPresent(facture::setUid);
        Optional.ofNullable((String) item.get("societe")).ifPresent(facture::setSociete);
        Optional.ofNullable((String) item.get("fournisseur")).ifPresent(facture::setFournisseur);
        Optional.ofNullable((String) item.get("date")).ifPresent(facture::setDate);
        Optional.ofNullable(item.get("montant"))
        .map(Object::toString)
        .ifPresent(facture::setMontant); 
        Optional.ofNullable((String) item.get("devise")).ifPresent(facture::setDevise);
        return facture;
    }
}