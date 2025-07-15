package com.example.Bpa_v2_bakc.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.scheduling.annotation.Scheduled;

import com.example.Bpa_v2_bakc.entities.mysql.Etat;
import com.example.Bpa_v2_bakc.entities.mysql.User;
import com.example.Bpa_v2_bakc.entities.sql_server.Demande;
import com.example.Bpa_v2_bakc.entities.sql_server.Detail;
import com.example.Bpa_v2_bakc.repositories.sql_server.DemandeRepository;
import com.example.Bpa_v2_bakc.repositories.sql_server.DetailRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class DetailService {
    private final DetailRepository detailRepository;
    private final DemandeRepository demandeRepository;
    private final ValidationService validationService;
    private final UserService userService;
    private final DemandeService demandeService;
    private final EmailService emailService;
    private final EtatService etatService;
    private final RestTemplate restTemplate;
    private final String pythonApiUrl;

    public DetailService(
        @Value("${python.api.url}") String pythonApiUrl,
        RestTemplate restTemplate,
        DetailRepository detailRepository,
        DemandeRepository demandeRepository,
        ValidationService validationService,
        DemandeService demandeService,     
        EmailService emailService,     
        UserService userService,
        EtatService etatService
    ){
        this.pythonApiUrl = pythonApiUrl;
        this.restTemplate = restTemplate;
        this.detailRepository = detailRepository;
        this.demandeRepository = demandeRepository;
        this.validationService = validationService;
        this.demandeService = demandeService;
        this.emailService = emailService;
        this.userService = userService;
        this.etatService = etatService;
    }

    public List<Detail> findByUid(String uid,String id_x3){
        return detailRepository.findByRefDemAndIdX3(uid,id_x3);
    }
    
    public List<Detail> findByRefDem(String uid){
        return detailRepository.findByRefDem(uid);
    }

    @Scheduled(fixedRate = 60000) // Exécution toutes les 5 minutes (300000 ms = 5 min)
    @Transactional
    public void importDetail(){
        String url = pythonApiUrl + "/details-da/";
        Etat etat = etatService.findById(1);
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response != null && "success".equals(response.get("status"))) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
            data.forEach(item -> {
                String uid = Optional.ofNullable((String) item.get("num_piece"))
                    .map(numPiece -> numPiece + (String) item.get("code_article"))
                    .orElse(null);
                if(!detailRepository.existsByUid(uid)){
                    Detail detail = mapToDetail(item);
                    detail.setEtat(etat);
                    detailRepository.save(detail);
                }
            });
        } else {
            throw new RuntimeException("Erreur lors de la récupération des données");
        }
    }

    private Detail mapToDetail(Map<String, Object> item) {
        Detail detail = new Detail();
        Optional.ofNullable((String) item.get("num_piece"))
        .map(numPiece -> numPiece + (String) item.get("code_article"))
        .ifPresent(detail::setUid);
        Optional.ofNullable((String) item.get("num_piece")).ifPresent(detail::setRef_dem);
        Optional.ofNullable((String) item.get("code_article")).ifPresent(detail::setCode_article);        
        Optional.ofNullable((String) item.get("quantite")).ifPresent(detail::setQuantite);        
        Optional.ofNullable(item.get("pu"))
        .map(Object::toString)
        .ifPresent(detail::setPu);
        Optional.ofNullable((String) item.get("objet")).ifPresent(detail::setObjet);        
        Optional.ofNullable((String) item.get("devise")).ifPresent(detail::setDevise);        
        Optional.ofNullable((String) item.get("fournisseur")).ifPresent(detail::setFournisseur);        
        Optional.ofNullable(item.get("montant"))
        .map(Object::toString)
        .ifPresent(detail::setMontant);        
        Optional.ofNullable((String) item.get("validateur")).ifPresent(detail::setId_x3);        

        return detail;
    }

    @Scheduled(fixedRate = 1800000) //(tous les 30 min)
    @Transactional
    public void updateArticle(){
        String url = pythonApiUrl + "/details-approve/";
        String motif = "Demande approuver sur Sage x3";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response != null && "success".equals(response.get("status"))) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
            data.forEach(item -> {
                String uid = Optional.ofNullable((String) item.get("num_piece"))
                .map(numPiece -> numPiece + (String) item.get("article"))
                .orElse(null);
                String id_x3 = (String) item.get("validateur");
                String action = (String) item.get("action");   
                Optional<Detail> det = detailRepository.findByUid(uid);
                Optional<User> user = userService.getUserByIdX3Opt(id_x3);
                int is_da = 3;
                if(det.isPresent() && user.isPresent()){
                    Detail detail = new Detail();
                    detail = det.get();
                    if (action.equals("REJ")) {
                        Etat etat = etatService.findById(3);
                        if(detail.getEtat().getIdEtat() != etat.getIdEtat()){
                            detail.setEtat(etat);
                            detailRepository.save(detail);
                            validationService.saveValidation(uid,detail.getObjet(),detail.getMontant(),motif,etat, user.get(),is_da);
                        }
                    }else if (action.equals("VAL")) {
                        Etat etat = etatService.findById(2);
                        if(detail.getEtat().getIdEtat() != etat.getIdEtat()){
                            detail.setEtat(etat);
                            detailRepository.save(detail);
                            validationService.saveValidation(uid,detail.getObjet(),detail.getMontant(),motif,etat, user.get(),is_da);
                        }
                    }
                }else{
                    System.out.println("L'utilisateur n'existe pas ou la detail n'est pas present");
                }
            });
        }
    }

    public String validationArticle(String uid,String ref_dem,String code_article, String idX3, int valeur1, int valeur2, int valeur3, String designation,int idetat,String motif){
        List<Detail> detailRejs = detailRepository.findByRefDemAndEtat(ref_dem, 4);
        List<Detail> details = detailRepository.findByRefDemAndEtat(ref_dem, 2);
        Demande demande = demandeService.findByUidAndX3(ref_dem,idX3);
        User user = userService.getUserByIdX3(idX3);
        int is_da = 3;
        int is_da_et = 2;
        if(details.size()<=1){
            if (detailRejs.isEmpty() && idetat == 2) {
                Etat etatval = etatService.findById(idetat);
                demande.setEtat(etatval);
                demandeRepository.save(demande);
                // validationService.saveValidation(uid,motif,etatval, user,is_da);
                // validationService.saveValidation(ref_dem,motif,etatval, user,is_da_et);
                valeur1 = 3;
            }
            if (detailRejs.isEmpty() && idetat == 3) {
                Etat etatval = etatService.findById(4);
                demande.setEtat(etatval);
                demandeRepository.save(demande);
                // validationService.saveValidation(uid,motif,etatval, user,is_da);
                // validationService.saveValidation(ref_dem,motif,etatval, user,is_da_et);
                valeur1 = 2;
            }else if(detailRejs.size() >= 1){
                Etat etatval = etatService.findById(4);
                demande.setEtat(etatval);
                demandeRepository.save(demande);
                // validationService.saveValidation(uid,motif,etatval, user,is_da);
                // validationService.saveValidation(ref_dem,motif,etatval, user,is_da_et);
                valeur1 = 2;
            }
        }else{
            valeur1= 2;
        }
        String url = pythonApiUrl +"/update_article/"; 
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("uid", ref_dem);
            bodyMap.put("code_article", code_article);
            bodyMap.put("valeur1", valeur1);
            bodyMap.put("valeur2", valeur2);
            bodyMap.put("valeur3", valeur3);
            bodyMap.put("id_x3", idX3);
            bodyMap.put("designation", designation);

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
            String subject,content;
            Etat etat = etatService.findById(idetat);
            Detail detail = findByUid(uid);
            detail.setEtat(etat);
            detailRepository.save(detail);
            validationService.saveValidation(uid,detail.getObjet(),detail.getMontant(), motif,etat, user,is_da);
            if(idetat == 2){
                subject = "[BPA] Demande N°" + detail.getRef_dem() + "article :"+detail.getCode_article()+"- Validation DA";
                content = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<style>"
                    + "  body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }"
                    + "  .header { color: #2c3e50; border-bottom: 1px solid #eee; padding-bottom: 10px; }"
                    + "  .content { margin: 20px 0; }"
                    + "  .footer { font-size: 0.9em; color: #7f8c8d; border-top: 1px solid #eee; padding-top: 10px; }"
                    + "  .highlight { background-color: #f8f9fa; padding: 10px; border-left: 3px solid #2ecc71; margin: 10px 0; }"
                    + "  .success-badge { background-color: #2ecc71; color: white; padding: 3px 8px; border-radius: 3px; font-size: 0.9em; }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='header'>"
                    + "  <h2>Demande BPA N°" + detail.getRef_dem() + "</h2>"
                    + "</div>"
                    + "<div class='content'>"
                    + "  <p>Bonjour " + demande.getDemandeur() + ",</p>"
                    + "  <div class='highlight'>"
                    + "    <p><span class='success-badge'>VALIDATION DE LA DEMANDE</span></p>"
                    + "    <p>Nous tiens à vous informer qu'un article "+detail.getCode_article()+" de votre demande a été validée par <strong>"
                    + user.getPrenom() + " " + user.getNom() + "</strong> .</p>"
                    + "  </div>"
                    + "  <p>Cette ligne comporte cet article est maintenant en attente de transformation en BC.</p>"
                    + "  <p>Cordialement,</p>"
                    + "</div>"
                    + "  <div class='footer'>"
                    + "    <p>© 2025 Plateforme d'approbation de BPA. Tous droits réservés.</p>"
                    + "    <p>Cet e-mail a été envoyé automatiquement, merci de ne pas y répondre.</p>"
                    + "  </div>"
                    + "</body>"
                    + "</html>";
            }else{
                subject = "[BPA] Demande N°" + detail.getRef_dem() + "article :"+detail.getCode_article()+"- Validation DA";
                content = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<style>"
                    + "  body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }"
                    + "  .header { color: #2c3e50; border-bottom: 1px solid #eee; padding-bottom: 10px; }"
                    + "  .content { margin: 20px 0; }"
                    + "  .footer { font-size: 0.9em; color: #7f8c8d; border-top: 1px solid #eee; padding-top: 10px; }"
                    + "  .highlight { background-color: #fef2f2; padding: 10px; border-left: 3px solid #ef4444; margin: 10px 0; }"
                    + "  .alert-badge { background-color: #ef4444; color: white; padding: 3px 8px; border-radius: 3px; font-size: 0.9em; }"
                    + "  .motif { background-color: #fff7ed; padding: 10px; border: 1px dashed #f59e0b; margin: 10px 0; font-style: italic; }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='header'>"
                    + "  <h2>Demande BPA N°" + detail.getRef_dem() + "</h2>"
                    + "</div>"
                    + "<div class='content'>"
                    + "  <p>Bonjour " + demande.getDemandeur() + ",</p>"
                    + "  <div class='highlight'>"
                    + "    <p><span class='alert-badge'>REJET DE LA DEMANDE</span></p>"
                    + "    <p>Nous tiens à vous informer qu'un article "+detail.getCode_article()+" de votre demande a été refusée par <strong>"
                    + user.getPrenom() + " " + user.getNom() + "</strong> .</p>"
                    + "  </div>"                    + "  <div class='motif'>"
                    + "    <p><strong>Motif du refus :</strong></p>"
                    + "    <p>\"" + motif + "\"</p>"
                    + "  </div>"
                    + "  <p>Je vous pris de procéder à une nouvelle demande et de prendre compte la remarque.</p>"
                    + "  <p>Cordialement,</p>"
                    + "</div>"
                    + "  <div class='footer'>"
                    + "    <p>© 2025 Plateforme d'approbation de BPA. Tous droits réservés.</p>"
                    + "    <p>Cet e-mail a été envoyé automatiquement, merci de ne pas y répondre.</p>"
                    + "  </div>"
                    + "</body>"
                    + "</html>";
            }

            emailService.sendEmail(demande.getEmail(), subject, content);
            return response.getBody();
        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }

    private Detail findByUid(String uid){
        return detailRepository.findByUid(uid)
        .orElseThrow(() -> new RuntimeException("detail non trouvé"));
    }
}
