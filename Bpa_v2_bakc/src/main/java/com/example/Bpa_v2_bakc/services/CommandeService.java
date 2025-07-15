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

import com.example.Bpa_v2_bakc.entities.mysql.Etat;
import com.example.Bpa_v2_bakc.entities.mysql.Finaldestination;
import com.example.Bpa_v2_bakc.entities.mysql.User;
import com.example.Bpa_v2_bakc.entities.sql_server.Commande;
import com.example.Bpa_v2_bakc.entities.sql_server.Detailbc;
import com.example.Bpa_v2_bakc.repositories.mysql.EtatRepository;
import com.example.Bpa_v2_bakc.repositories.mysql.UserRepository;
import com.example.Bpa_v2_bakc.repositories.mysql.ValidationRepository;
import com.example.Bpa_v2_bakc.repositories.sql_server.CommandeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class CommandeService {
    private final String pythonApiUrl;
    private final RestTemplate restTemplate;
    private final CommandeRepository commandeRepository;
    private final ValidationRepository validationRepository;
    private final UserRepository userRepository;
    private final ValidationService validationService;
    private final EtatRepository etatRepository;
    private final EmailService emailService;
    private final DetailbcService detailbcService;
    private final FinalDesignationService finalDesignationService;

    public CommandeService(
        @Value("${python.api.url}") String pythonApiUrl,
        RestTemplate restTemplate,
        CommandeRepository commandeRepository,
        ValidationRepository validationRepository,
        ValidationService validationService,
        UserRepository userRepository,
        EmailService emailService,
        EtatRepository etatRepository,
        DetailbcService detailbcService,
        FinalDesignationService finalDesignationService
    ){
        this.commandeRepository = commandeRepository;
        this.validationRepository = validationRepository;
        this.validationService=validationService;
        this.userRepository = userRepository;
        this.pythonApiUrl = pythonApiUrl;
        this.etatRepository = etatRepository;
        this.emailService = emailService;
        this.restTemplate = restTemplate;
        this.detailbcService = detailbcService;    
        this.finalDesignationService = finalDesignationService;    
    }

    public Page<Commande> findAll(String id_x3,Pageable pageable,String critere){
        String cri = "%"+critere+"%";
        return commandeRepository.findByidX3(pageable, id_x3, cri);
    }
    
    public Page<Commande> findForLecteur(Pageable pageable,String critere,String validateur,int etat){
        return commandeRepository.findAllPage(pageable, critere,validateur,etat);
    }

    @Scheduled(fixedRate = 60000) // Exécution toutes les 5 minutes (300000 ms = 5 min)
    @Transactional
    public void importCommande(){
        String url = pythonApiUrl + "/commandes/";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Optional<Etat> etat = etatRepository.findById(1);
        System.out.println("naka aho tompoko???????");
        if (response != null && "success".equals(response.get("status"))) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
            
            data.forEach(item -> {
                String uid = (String) item.get("uid");
                String id_x3_1 = (String) item.get("validateur");
                String signature = (String) item.get("signature");
                String[] id_x3;
                if (id_x3_1 != null && id_x3_1.contains(",")) {
                    id_x3 = id_x3_1.split(",");
                } else {
                    id_x3 = new String[]{id_x3_1};
                }
                for (String id : id_x3) {
                    Optional<Commande> com = commandeRepository.findByUidAndId_x3(uid, id);
                    if (com.isEmpty()) {
                        Commande commande = mapToCommande(item);
                        if (signature.equals("PARTIELLEMENT")){
                            Optional<Etat> etatPart = etatRepository.findById(4);
                            if (etatPart.isPresent()) {
                                commande.setEtat(etatPart.get());
                            }
                        }else if (etat.isPresent() && signature.equals("NON")) {
                            commande.setEtat(etat.get());
                        }
                        commande.setId_x3(id);
                        commandeRepository.save(commande);
                    }
                }
            });
        } else {
            throw new RuntimeException("Erreur lors de la récupération des données");
        }
    }

    private Commande mapToCommande(Map<String, Object> item) {
        Commande commande = new Commande();
        Optional.ofNullable((String) item.get("uid")).ifPresent(commande::setUid);
        Optional.ofNullable((String) item.get("societe")).ifPresent(commande::setSociete);
        Optional.ofNullable((String) item.get("demandeur")).ifPresent(commande::setDemandeur);   
        Optional.ofNullable((String) item.get("email")).ifPresent(commande::setEmail);
        Optional.ofNullable((String) item.get("lien_pj")).ifPresent(commande::setLienPj);        
        Optional.ofNullable((String) item.get("date")).ifPresent(commande::setDate);        
        Optional.ofNullable((String) item.get("devise")).ifPresent(commande::setDevise);        
        Optional.ofNullable((String) item.get("signature")).ifPresent(commande::setSignature);        
        Optional.ofNullable((String) item.get("societe")).ifPresent(commande::setSociete);        
        Optional.ofNullable((String) item.get("code_payement")).ifPresent(commande::setCodepayement);        
        Optional.ofNullable((String) item.get("mode_payement")).ifPresent(commande::setModepayement);        
        Optional.ofNullable((String) item.get("code_anal")).ifPresent(commande::setCodeA);        
        Optional.ofNullable((String) item.get("fournisseur")).ifPresent(commande::setBeneficiaire);        
        Optional.ofNullable((String) item.get("nom_validateur")).ifPresent(commande::setValidateur);        
        Optional.ofNullable(item.get("montant"))
        .map(Object::toString)
        .ifPresent(commande::setMontant);   

        return commande;
    }

    public Commande findByUidIdX3(String uid, String id_x3){
        Optional<Commande> comOptional = commandeRepository.findByUidIdX3(uid, id_x3);
        if (comOptional.isPresent()) {
            return comOptional.get();
        }else{
            return null;
        }
    }

    public List<Commande> findByUid(String uid){
        return commandeRepository.findByUid(uid);
    }
    
    public List<Commande> findByUidAndIdx3(String uid,String id_x3){
        return commandeRepository.findByUidAndIdX3(uid,id_x3);
    }
    
    public List<Commande> findByUidNoIdx3(String uid,String id_x3){
        return commandeRepository.findByUidNoIdX3(uid,id_x3);
    }
    
    public Commande findByUidAndX3(String uid,String id_x3){
        return commandeRepository.findByUidAndId_x3(uid,id_x3)
        .orElseThrow(() -> new RuntimeException("Commande non trouvé"));
    }

    public String validerCommande(String uid, String idX3, int valeur1, String designation,int idetat,String motif){
        String url = pythonApiUrl +"/update_commande/"; 
        try {
            String subject,content;
            Optional<User> user = userRepository.findByIdx3(idX3);
            Etat etat = etatRepository.findById(idetat).orElseThrow(() -> new RuntimeException("demande non trouvé"));
            Commande cmd = findByUidAndX3(uid,idX3);
            List<Commande> commandes = findByUidNoIdx3(uid,idX3);
            List<Detailbc> details = detailbcService.findByRefDemuid(cmd.getUid());
            String montant =cmd.getMontant();
            String objet = getArticle(details);
            String des;
            if (checkPR(details)) {
                des = "pr";
            }else{
                des = "fin";
            }
            Finaldestination finaldestination = finalDesignationService.getByUid(des);
            int is_da = 2;
            if(user.isPresent()){
                if(idetat == 3){
                    cmd.setEtat(etat);
                    commandeRepository.save(cmd);
                    validationService.saveValidation(uid,objet,montant, motif,etat, user.get(),is_da);
                    if (commandes.size() >= 1) {
                        for (Commande commande : commandes) {
                            commande.setEtat(etat);
                            commandeRepository.save(commande);
                            System.out.println("refuuuuuuuuuussss");
                        }
                    }
                    subject = "[BPA] Bon de commande N°" + cmd.getUid() + " - Notification de refus BC";

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
                        + "  <h2>Bon de Commande BPA N°" + cmd.getUid() + "</h2>"
                        + "</div>"
                        + "<div class='content'>"
                        + "  <p>Bonjour " + cmd.getDemandeur() + ",</p>"
                        + "  <div class='highlight'>"
                        + "    <p><span class='alert-badge'>COMMANDE REFUSÉE</span></p>"
                        + "    <p>Votre demande a été examinée et refusée au niveau de l'approbation BC par <strong>"
                        + user.get().getPrenom() + " " + user.get().getNom() + "</strong>.</p>"
                        + "  </div>"
                        + "  <div class='motif'>"
                        + "    <p><strong>Motif du refus :</strong></p>"
                        + "    <p>\"" + motif + "\"</p>"
                        + "  </div>"
                        + "  <p>Je vous pris de procéder à une nouvelle demande.</p>"
                        + "  <p>Cordialement,</p>"
                        + "</div>"
                        + "  <div class='footer'>"
                        + "    <p>© 2025 Plateforme d'approbation de BPA. Tous droits réservés.</p>"
                        + "    <p>Cet e-mail a été envoyé automatiquement, merci de ne pas y répondre.</p>"
                        + "  </div>"
                        + "</body>"
                        + "</html>";
                }else{
                    if(cmd.getEtat().getIdEtat() ==1){
                        if (commandes.size() >= 1) {
                            Etat etatPart = etatRepository.findById(2).orElseThrow(() -> new RuntimeException("demande non trouvé"));
                            for (Commande commande : commandes) {
                                commande.setEtat(etatPart);
                                commandeRepository.save(commande);
                            }
                            cmd.setEtat(etat);
                            commandeRepository.save(cmd);
                            validationService.saveValidation(uid,objet,montant, motif,etat, user.get(),is_da);
                            valeur1 = 3;
                        }else if(commandes.size() < 1){
                            cmd.setEtat(etat);
                            commandeRepository.save(cmd);
                            validationService.saveValidation(uid,objet,montant, motif,etat, user.get(),is_da);
                            valeur1 = 3;
                        }
                    }else{
                        if (commandes.size() >= 1) {
                            for (Commande commande : commandes) {
                                commande.setEtat(etat);
                                commandeRepository.save(commande);
                            }
                        }
                        cmd.setEtat(etat);
                        commandeRepository.save(cmd);
                        validationService.saveValidation(uid,objet,montant, motif,etat, user.get(),is_da);
                    }
                    subject = "[BPA] Bon de commande N°" + cmd.getUid() + " - Validation BC";
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
                        + "  <h2>Bon de commande BPA N°" + cmd.getUid() + "</h2>"
                        + "</div>"
                        + "<div class='content'>"
                        + "  <p>Bonjour " + cmd.getDemandeur() + ",</p>"
                        + "  <div class='highlight'>"
                        + "    <p><span class='success-badge'>VALIDATION DE LA COMMANDE</span></p>"
                        + "    <p>Nous avons le plaisir de vous informer que votre demande a été validée définitivement par <strong>"
                        + user.get().getPrenom() + " " + user.get().getNom() + "</strong> .</p>"
                        + "  </div>"
                        + "  <p>Vous etes autorisé la décaissement.</p>"
                        + "  <p>Cordialement,</p>"
                        + "</div>"
                        + "  <div class='footer'>"
                        + "    <p>© 2025 Plateforme d'approbation de BPA. Tous droits réservés.</p>"
                        + "    <p>Cet e-mail a été envoyé automatiquement, merci de ne pas y répondre.</p>"
                        + "  </div>"
                        + "</body>"
                        + "</html>";

                    String content1 = "<!DOCTYPE html>"
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
                        + "  <h2>Bon de commande BPA N°" + cmd.getUid() + "</h2>"
                        + "</div>"
                        + "<div class='content'>"
                        + "  <p>Demande de " + cmd.getDemandeur() + ",</p>"
                        + "  <div class='highlight'>"
                        + "    <p><span class='success-badge'>VALIDATION DE LA COMMANDE</span></p>"
                        + "    <p>Nous avons le plaisir de vous informer que le bon de commande a été validé définitivement par <strong>"
                        + user.get().getPrenom() + " " + user.get().getNom() + "</strong> .</p>"
                        + "  </div>"
                        + "  <p>C'est autorisé à la décaissement.</p>"
                        + "  <p>Cordialement,</p>"
                        + "</div>"
                        + "  <div class='footer'>"
                        + "    <p>© 2025 Plateforme d'approbation de BPA. Tous droits réservés.</p>"
                        + "    <p>Cet e-mail a été envoyé automatiquement, merci de ne pas y répondre.</p>"
                        + "  </div>"
                        + "</body>"
                        + "</html>";
                        
                        emailService.sendEmail(finaldestination.getDesignation(),subject,content1);

                }
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("uid", uid);
                bodyMap.put("valeur1", valeur1);
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
                    
                emailService.sendEmail(cmd.getEmail(),subject,content);
                return response.getBody();
            }else{
                return "l'utilisateur en question n'existe pas";
            }
        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }

    private Boolean checkPR(List<Detailbc> detailbcs) {
        for (Detailbc detailbc : detailbcs) {
            if ("MP-000003".equals(detailbc.getCode_article())) {
                return true;
            }
        }
        return false;
    }    

    private String getArticle(List<Detailbc> detailbcs) {
        if (detailbcs == null || detailbcs.isEmpty()) {
            return "";
        }

        if (detailbcs.size() == 1) {
            return detailbcs.get(0).getObjet();
        } else {
            return detailbcs.stream()
                            .map(Detailbc::getObjet)
                            .collect(Collectors.joining(", "));
        }
    }

    @Scheduled(fixedRate = 1800000) //(tous les 30 min)
    @Transactional()
    public void updateCommande(){
        String url = pythonApiUrl + "/commandes-approve/";
        String motif = "Commande approuver sur Sage x3";
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && "success".equals(response.get("status"))) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");

                data.forEach(item -> {
                    String uid = (String) item.get("num_piece");
                    String id_x3 = (String) item.get("validateur");
                    String action = (String) item.get("action");
                    String signature = (String) item.get("signature");
                    Optional<User> user = userRepository.findByIdx3(id_x3);
                    int is_da = 2;
                    Optional<Commande> OptCommande = commandeRepository.findByUidAndId_x3(uid,id_x3);
                    if(OptCommande.isPresent() && user.isPresent()){
                        Commande commande = OptCommande.get();
                        List<Detailbc> details = detailbcService.findByRefDemuid(commande.getUid());
                        String montant =commande.getMontant();
                        String objet = getArticle(details);
                        Etat etat = getEtatByAction(action, signature);
                        if (etat != null && !validationRepository.existsByDemandeAndUserAndEtat(uid,user.get(), etat)) {
                            commande.setEtat(etat);
                            commandeRepository.save(commande);
                            validationService.saveValidation(uid,objet,montant,motif, etat,user.get(),is_da);
                        }
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des commandes: " + e.getMessage());
        }
    }

    private Etat getEtatByAction(String action,String signature) {
        if ("REJ".equals(action)) {
            return etatRepository.findById(3).orElse(null);
        } else if ("VAL".equals(action)) {
            // if ("PARTIELLEMENT".equals(signature)) {
            //     return etatRepository.findById(5).orElse(null);
            // }else{
                return etatRepository.findById(2).orElse(null); 
            // }
        }
        return null;
    }
}
