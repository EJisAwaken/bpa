package com.example.Bpa_v2_bakc.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Bpa_v2_bakc.entities.sql_server.Detailbc;
import com.example.Bpa_v2_bakc.repositories.sql_server.DetailbcRepository;

import jakarta.transaction.Transactional;

@Service
public class DetailbcService {
    private final DetailbcRepository detailbcRepository;
    private final RestTemplate restTemplate;
    private final String pythonApiUrl;

    public DetailbcService(
        @Value("${python.api.url}") String pythonApiUrl,
        RestTemplate restTemplate,
        DetailbcRepository detailbcRepository
    ){
        this.pythonApiUrl = pythonApiUrl;
        this.restTemplate = restTemplate;
        this.detailbcRepository = detailbcRepository;
    }

    @Scheduled(fixedRate = 60000) // Exécution toutes les 5 minutes (300000 ms = 5 min)
    @Transactional
    public void importDetailBc(){
        String url = pythonApiUrl + "/details-bc/";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response != null && "success".equals(response.get("status"))) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
            data.forEach(item -> {
                String uid = Optional.ofNullable((String) item.get("num_piece"))
                    .map(numPiece -> numPiece + (String) item.get("code_article"))
                    .orElse(null);
                    System.out.println("detaillllll    bcccc :" + uid);
                    if(!detailbcRepository.existsByUid(uid)){
                        Detailbc detailBc = mapToDetailBc(item);
                        detailbcRepository.save(detailBc);
                    }
            });
        }else {
            throw new RuntimeException("Erreur lors de la récupération des données");
        }
    }

    private Detailbc mapToDetailBc(Map<String, Object> item) {
        Detailbc detailBc = new Detailbc();
        Optional.ofNullable((String) item.get("num_piece"))
        .map(numPiece -> numPiece + (String) item.get("code_article"))
        .ifPresent(detailBc::setUid);
        Optional.ofNullable((String) item.get("num_piece")).ifPresent(detailBc::setRef_dem);
        Optional.ofNullable((String) item.get("code_article")).ifPresent(detailBc::setCode_article);        
        Optional.ofNullable((String) item.get("quantite")).ifPresent(detailBc::setQuantite);        
        Optional.ofNullable(item.get("pu"))
        .map(Object::toString)
        .ifPresent(detailBc::setPu);
        Optional.ofNullable((String) item.get("objet")).ifPresent(detailBc::setObjet);        
        Optional.ofNullable((String) item.get("devise")).ifPresent(detailBc::setDevise);        
        Optional.ofNullable(item.get("montant"))
        .map(Object::toString)
        .ifPresent(detailBc::setMontant);      

        return detailBc;
    }

    public List<Detailbc> findByRefDemuid(String uid){
        return detailbcRepository.findByRefDem(uid);
    }

}
