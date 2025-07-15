package com.example.Bpa_v2_bakc.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.example.Bpa_v2_bakc.entities.sql_server.Detailmere;
import com.example.Bpa_v2_bakc.repositories.sql_server.DetailmereRepository;


@Service
public class DetailmereService {
    private final DetailmereRepository detailmereRepository;
    private final RestTemplate restTemplate;
    private final String pythonApiUrl;

    public DetailmereService(
        @Value("${python.api.url}") String pythonApiUrl,
        RestTemplate restTemplate,
        DetailmereRepository detailmereRepository
    ){
        this.pythonApiUrl = pythonApiUrl;
        this.restTemplate = restTemplate;
        this.detailmereRepository = detailmereRepository;
    }

    public List<Detailmere> getByMere(String mere){
        return detailmereRepository.findByMere(mere);
    }

    @Scheduled(fixedRate = 60000) // Exécution toutes les 5 minutes (300000 ms = 5 min)
    @Transactional
    public void importDetailmere(){
        String url = pythonApiUrl + "/details-mere/";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response != null && "success".equals(response.get("status"))) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
            data.forEach(item -> {
                String uid = Optional.ofNullable((String) item.get("uid"))
                    .map(numPiece -> numPiece + (String) item.get("code_article"))
                    .orElse(null);
                if (!detailmereRepository.existsByUid(uid)) {
                    Detailmere detailmere = mapToDetailmere(item);
                    detailmereRepository.save(detailmere);
                }
            });
        }
    }

    private Detailmere mapToDetailmere(Map<String, Object> item){
        Detailmere detailmere = new Detailmere();
        Optional.ofNullable((String) item.get("uid"))
        .map(numPiece -> numPiece + (String) item.get("code_article"))
        .ifPresent(detailmere::setUid);
        Optional.ofNullable((String) item.get("uid")).ifPresent(detailmere::setMere);
        Optional.ofNullable((String) item.get("societe")).ifPresent(detailmere::setSociete);
        Optional.ofNullable((String) item.get("fournisseur")).ifPresent(detailmere::setFournisseur);
        Optional.ofNullable((String) item.get("code_article")).ifPresent(detailmere::setCode_article);    
        Optional.ofNullable((String) item.get("article")).ifPresent(detailmere::setObjet);   
        Optional.ofNullable(item.get("quantite"))
        .map(Object::toString)
        .ifPresent(detailmere::setQuantite);    
        Optional.ofNullable(item.get("montant"))
        .map(Object::toString)
        .ifPresent(detailmere::setMontant);    
        Optional.ofNullable(item.get("pu"))
        .map(Object::toString)
        .ifPresent(detailmere::setPu);
        Optional.ofNullable((String) item.get("devise")).ifPresent(detailmere::setDevise);   
        return detailmere;  
    }
}
