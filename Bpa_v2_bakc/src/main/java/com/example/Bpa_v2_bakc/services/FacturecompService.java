package com.example.Bpa_v2_bakc.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.Bpa_v2_bakc.entities.sql_server.Facturecomp;
import com.example.Bpa_v2_bakc.repositories.sql_server.FacturecompRepository;


import jakarta.transaction.Transactional;

@Service
public class FacturecompService {
    private final FacturecompRepository facturecompRepository;
    private final RestTemplate restTemplate;
    private final String pythonApiUrl;

    public FacturecompService(
        @Value("${python.api.url}") String pythonApiUrl,
        RestTemplate restTemplate,
        FacturecompRepository facturecompRepository
    ){
        this.pythonApiUrl = pythonApiUrl;
        this.restTemplate = restTemplate;
        this.facturecompRepository = facturecompRepository;
    }

    public List<Facturecomp> getByMere(String mere){
        return facturecompRepository.findByMere(mere);
    }

    @Scheduled(fixedRate = 60000) // Exécution toutes les 5 minutes (300000 ms = 5 min)
    @Transactional
    public void importFacturecomp(){
        String url = pythonApiUrl + "/factures-comp/";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        // Optional<Etat> etat = etatRepository.findById(6);
        if (response != null && "success".equals(response.get("status"))) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
            data.forEach(item -> {
                String uid = Optional.ofNullable((String) item.get("uid"))
                    .map(numPiece -> numPiece + (String) item.get("code"))
                    .orElse(null);
                String reffc = (String) item.get("uid");
                String mere = (String) item.get("ref_dem");
                String code = (String) item.get("code");
                int etatx3 = (int) item.get("etat");
                // Etat etat =etatRepository.findById(6).orElse(null);
                if(!facturecompRepository.existsByReffcAndMereAndCode(reffc,mere,code) && etatx3 == 1){
                    Facturecomp facturecomp = mapToFacturecomp(item);
                    facturecomp.setUid(uid);
                    facturecompRepository.save(facturecomp);
                }
            });
        }
    }

    public List<Facturecomp> getByUid(String uid){
        return facturecompRepository.findAllByReffc(uid);
    }

    private Facturecomp mapToFacturecomp(Map<String, Object> item){
        Facturecomp facturecomp = new Facturecomp();
        Optional.ofNullable((String) item.get("uid")).ifPresent(facturecomp::setReffc);
        Optional.ofNullable((String) item.get("ref_dem")).ifPresent(facturecomp::setMere);
        Optional.ofNullable((String) item.get("societe")).ifPresent(facturecomp::setSociete);
        Optional.ofNullable((String) item.get("fournisseur")).ifPresent(facturecomp::setFournisseur);
        Optional.ofNullable((String) item.get("code")).ifPresent(facturecomp::setCode);
        Optional.ofNullable((String) item.get("date")).ifPresent(facturecomp::setDate);
        Optional.ofNullable((String) item.get("article")).ifPresent(facturecomp::setObjet);
        Optional.ofNullable(item.get("montant"))
        .map(Object::toString)
        .ifPresent(facturecomp::setMontant); 
        Optional.ofNullable((String) item.get("devise")).ifPresent(facturecomp::setDevise);
        return facturecomp;
    }

    // public String validation_facture(String uid,String mere,String idX3, int etat,int value,String motif){
    //     List<Facturecomp> facturecomps = facturecompRepository.findByMereAndEtat(mere);
    //     Optional<Facture> factureOptional = factureRepository.findByUid(mere);
    //     Optional<Facturecomp> facOptional = facturecompRepository.findByUid(uid);
    //     Optional<Etat> eOptional = etatRepository.findById(etat);
    //     Optional<User> user = userRepository.findByIdx3(idX3);
    //     String url = pythonApiUrl +"/update_facture/"; 
    //     int is_da =4;
    //     if(facOptional.isPresent() && eOptional.isPresent() && user.isPresent() && factureOptional.isPresent()){
    //         Facture facture = factureOptional.get();
    //         try{
    //             if (facturecomps.size() <= 1) {
    //                 if (etat == 7) {
    //                     Optional<Etat> etatOptional = etatRepository.findById(3);
    //                     facture.setEtat(etatOptional.get());
    //                     factureRepository.save(facture);   
    //                 }else{
    //                     Optional<Etat> etatOptional = etatRepository.findById(4);
    //                     facture.setEtat(etatOptional.get());
    //                     factureRepository.save(facture);
    //                 }
    //             }else{
    //                 Optional<Etat> etatOptional = etatRepository.findById(5);
    //                 facture.setEtat(etatOptional.get());
    //                 factureRepository.save(facture); 
    //             }
    //             ObjectMapper mapper = new ObjectMapper();
    //             Map<String, Object> bodyMap = new HashMap<>();
    //             bodyMap.put("uid", uid);
    //             bodyMap.put("valeur1", value);

    //             String json = mapper.writeValueAsString(bodyMap);

    //             HttpHeaders headers = new HttpHeaders();
    //             headers.setContentType(MediaType.APPLICATION_JSON);

    //             HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

    //             ResponseEntity<String> response = restTemplate.exchange(
    //                 url,
    //                 HttpMethod.PUT,
    //                 requestEntity,
    //                 String.class
    //             );
    //             Facturecomp facturecomp = facOptional.get();
    //             facturecomp.setEtat(eOptional.get());
    //             validationService.saveValidation(uid, motif,eOptional.get(), user.get(),is_da);
    //             facturecompRepository.save(facturecomp);
    //             return response.getBody();
    //         } catch (Exception e) {
    //             return "Erreur : " + e.getMessage();
    //         }
    //     }else{
    //         return "l'utilisateur en question n'existe pas";
    //     }
    // }
}
