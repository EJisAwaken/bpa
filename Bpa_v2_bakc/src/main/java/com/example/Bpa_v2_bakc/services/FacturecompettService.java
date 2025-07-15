package com.example.Bpa_v2_bakc.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

import com.example.Bpa_v2_bakc.entities.mysql.Etat;
import com.example.Bpa_v2_bakc.entities.mysql.User;
import com.example.Bpa_v2_bakc.entities.sql_server.Facturecomp;
import com.example.Bpa_v2_bakc.entities.sql_server.Facturecompett;
import com.example.Bpa_v2_bakc.repositories.mysql.EtatRepository;
import com.example.Bpa_v2_bakc.repositories.mysql.UserRepository;
import com.example.Bpa_v2_bakc.repositories.sql_server.FacturecompRepository;
import com.example.Bpa_v2_bakc.repositories.sql_server.FacturecompettRepository;

@Service
public class FacturecompettService {
    private final FacturecompettRepository facturecompettRepository;
    private final FacturecompRepository facturecompRepository;
    private final EtatRepository etatRepository;
    private final UserRepository userRepository;
    private final ValidationService validationService;
    private final RestTemplate restTemplate;
    private final String pythonApiUrl;

    public FacturecompettService(
        @Value("${python.api.url}") String pythonApiUrl,
        RestTemplate restTemplate,
        FacturecompRepository facturecompRepository,
        UserRepository userRepository,
        FacturecompettRepository facturecompettRepository,
        ValidationService validationService,
        EtatRepository etatRepository
    ){
        this.pythonApiUrl = pythonApiUrl;
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.validationService = validationService;
        this.facturecompettRepository = facturecompettRepository;
        this.facturecompRepository = facturecompRepository;
        this.etatRepository = etatRepository;
    }

    public Page<Facturecompett> findAllPage(Pageable pageable, String critere){
        return facturecompettRepository.findAllPage(pageable, critere);
    }

    public Page<Facturecompett> findAllForLecPage(Pageable pageable, String critere,int etat){
        return facturecompettRepository.findAllForLecPage(pageable, critere,etat);
    }

    public Facturecompett getByUid(String uid){
        Optional<Facturecompett> fOptional = facturecompettRepository.findByUid(uid);
        if(fOptional.isPresent()){
            return fOptional.get();
        }else{
            return null;
        }
    }

    @Scheduled(fixedRate = 60000) // Exécution toutes les 5 minutes (300000 ms = 5 min)
    @Transactional
    public void importFacturecompett(){
        String url = pythonApiUrl + "/factures-comp-ett/";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        // Optional<Etat> etat = etatRepository.findById(6);
        Etat etat =etatRepository.findById(1).orElse(null);
        if (response != null && "success".equals(response.get("status"))) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
            data.forEach(item -> {
                String uid = (String) item.get("uid");
                
                if(!facturecompettRepository.existsByUid(uid)){
                    Facturecompett facturecomp = mapToFacturecomp(item);
                    facturecomp.setEtat(etat);
                    facturecompettRepository.save(facturecomp);
                }
            });
        }
    }

    private Facturecompett mapToFacturecomp(Map<String, Object> item){
        Facturecompett facturecomp = new Facturecompett();
        Optional.ofNullable((String) item.get("uid")).ifPresent(facturecomp::setUid);
        Optional.ofNullable((String) item.get("societe")).ifPresent(facturecomp::setSociete);
        Optional.ofNullable((String) item.get("fournisseur")).ifPresent(facturecomp::setFournisseur);
        Optional.ofNullable((String) item.get("date")).ifPresent(facturecomp::setDate);
        Optional.ofNullable(item.get("montant"))
        .map(Object::toString)
        .ifPresent(facturecomp::setMontant); 
        Optional.ofNullable((String) item.get("devise")).ifPresent(facturecomp::setDevise);
        return facturecomp;
    }

        private String getArticle(List<Facturecomp> details) {
        if (details == null || details.isEmpty()) {
            return "";
        }

        if (details.size() == 1) {
            return details.get(0).getObjet();
        } else {
            return details.stream()
                            .map(Facturecomp::getObjet)
                            .collect(Collectors.joining(", "));
        }
    }

    public String validation(String uid,String idX3,int etat,int value,String motif){
        Facturecompett facturecompett= getByUid(uid);
        List<Facturecomp> details = facturecompRepository.findAllByReffc(uid);
        String objet = getArticle(details);
        String montant = facturecompett.getMontant();
        Optional<Etat> eOptional = etatRepository.findById(etat);
        Optional<User> user = userRepository.findByIdx3(idX3);
        String url = pythonApiUrl +"/update_facture/"; 
        int is_da =4;
        if (user.isPresent() && eOptional.isPresent()) {
            try{
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("uid", uid);
                bodyMap.put("valeur1", value);

                String json = mapper.writeValueAsString(bodyMap);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

                ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    requestEntity,
                    String.class
                );
                facturecompett.setEtat(eOptional.get());
                validationService.saveValidation(uid,objet,montant,motif,eOptional.get(), user.get(),is_da);
                facturecompettRepository.save(facturecompett);
                return response.getBody();
            } catch (Exception e) {
                return "Erreur : " + e.getMessage();
            }
        }else{
            return "l'utilisateur en question n'existe pas";
        }
    }
}