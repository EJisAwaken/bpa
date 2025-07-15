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
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Bpa_v2_bakc.entities.mysql.Etat;
import com.example.Bpa_v2_bakc.entities.mysql.User;
import com.example.Bpa_v2_bakc.entities.sql_server.Demande;
import com.example.Bpa_v2_bakc.entities.sql_server.Detail;
import com.example.Bpa_v2_bakc.entities.sql_server.Detailbc;
import com.example.Bpa_v2_bakc.repositories.mysql.EtatRepository;
import com.example.Bpa_v2_bakc.repositories.mysql.UserRepository;
import com.example.Bpa_v2_bakc.repositories.mysql.ValidationRepository;
import com.example.Bpa_v2_bakc.repositories.sql_server.DemandeRepository;
import com.example.Bpa_v2_bakc.repositories.sql_server.DetailRepository;

import org.springframework.transaction.annotation.Transactional;
@Service
public class DemandeService {
    private final String pythonApiUrl;
    private final RestTemplate restTemplate;
    private final DemandeRepository demandeRepository;
    private final DetailRepository detailRepository;
    private final EtatRepository etatRepository;
    private final ValidationService validationService;
    private final ValidationRepository validationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public DemandeService(
        @Value("${python.api.url}") String pythonApiUrl,
        RestTemplate restTemplate,
        DemandeRepository demandeRepository,
        DetailRepository detailRepository,
        ValidationService validationService,
        ValidationRepository validationRepository,
        UserRepository userRepository,
        EmailService emailService,
        EtatRepository etatRepository) {
            this.pythonApiUrl = pythonApiUrl;
            this.restTemplate = restTemplate;
            this.demandeRepository=demandeRepository;
            this.detailRepository=detailRepository;
            this.etatRepository=etatRepository;
            this.validationService=validationService; 
            this.validationRepository=validationRepository; 
            this.userRepository=userRepository;
            this.emailService=emailService;
        }

    public Page<Demande> findAll(String id_x3,Pageable pageable,String critere){
        return demandeRepository.findByidX3(pageable,id_x3,critere);
    }
    
    public Page<Demande> findAllPage(Pageable pageable,String critere,String validateur,int etat){
        return demandeRepository.findAllPage(pageable,critere,validateur,etat);
    }

    @Scheduled(fixedRate = 60000) // Exécution toutes les 5 minutes (300000 ms = 5 min)
    @Transactional
    public void importDemande(){
        String url = pythonApiUrl + "/demandes/";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Optional<Etat> etat = etatRepository.findById(1);
        if (response != null && "success".equals(response.get("status"))) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
            
            data.forEach(item -> {
                String uid = (String) item.get("uid");
                String id_x3 = (String) item.get("validateur");
                Optional<Demande> dem = demandeRepository.findByUidAndIdX3(uid,id_x3);
                if (dem.isEmpty() && etat.isPresent()) {
                    Demande demande = mapToDemande(item);
                    demande.setEtat(etat.get());
                    demandeRepository.save(demande);
                }
            });
        } else {
            throw new RuntimeException("Erreur lors de la récupération des données");
        }
    }

    
    private Demande mapToDemande(Map<String, Object> item) {
        Demande demande = new Demande();
        Optional.ofNullable((String) item.get("uid")).ifPresent(demande::setUid);
        Optional.ofNullable((String) item.get("societe")).ifPresent(demande::setSociete);
        Optional.ofNullable((String) item.get("demandeur")).ifPresent(demande::setDemandeur);   
        Optional.ofNullable((String) item.get("email")).ifPresent(demande::setEmail);
        Optional.ofNullable((String) item.get("beneficiaire")).ifPresent(demande::setBeneficiaire);
        Optional.ofNullable((String) item.get("lienPJ")).ifPresent(demande::setLienPj);        
        Optional.ofNullable((String) item.get("date")).ifPresent(demande::setDate);        
        Optional.ofNullable((String) item.get("signature")).ifPresent(demande::setSignature);        
        Optional.ofNullable((String) item.get("code_anal")).ifPresent(demande::setCodeA);        
        Optional.ofNullable((String) item.get("societe")).ifPresent(demande::setSociete);        
        Optional.ofNullable((String) item.get("devise")).ifPresent(demande::setDevise);        
        Optional.ofNullable((String) item.get("nom_validateur")).ifPresent(demande::setValidateur);        
        Optional.ofNullable(item.get("montant"))
        .map(Object::toString)
        .ifPresent(demande::setMontant);        
        Optional.ofNullable((String) item.get("validateur")).ifPresent(demande::setId_x3);        

        return demande;
    }

    @Scheduled(fixedRate = 1800000) //(tous les 30 min)
    @Transactional(rollbackFor = Exception.class)
    public void updateRequisition() {
        String url = pythonApiUrl + "/demandes-approve/";
        String motif = "Demande approuver sur Sage x3";

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && "success".equals(response.get("status"))) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");

                data.forEach(item -> {
                    String uid = (String) item.get("num_piece");
                    String id_x3 = (String) item.get("validateur");
                    String action = (String) item.get("action");

                    Optional<User> user = userRepository.findByIdx3(id_x3);
                    Optional<Demande> optionalDemande = demandeRepository.findByUidAndIdX3(uid, id_x3);
                    int is_da = 1;

                    if (optionalDemande.isPresent() && user.isPresent()) {
                        Demande demande = optionalDemande.get();
                        Etat etat = getEtatByAction(action); 
                        List<Detail> details = detailRepository.findByRefDemAndIdX3AndNotApprouve(demande.getUid(), demande.getId_x3());
                        String objet = getArticle(details);
                        String montant = demande.getMontant();
                        if (etat != null && !validationRepository.existsByDemandeAndUserAndEtat(uid,user.get(), etat)) {
                            demande.setEtat(etat);
                            demandeRepository.save(demande);
                            validationService.saveValidation(uid,objet,montant, motif, etat, user.get(),is_da);
                        }
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des demandes: " + e.getMessage());
        }
    }

    private Etat getEtatByAction(String action) {
        if ("N".equals(action)) {
            return etatRepository.findById(3).orElse(null);
        } else if ("T".equals(action)) {
            return etatRepository.findById(2).orElse(null);
        }else if ("P".equals(action)) {
            return etatRepository.findById(4).orElse(null);
        }
        return null;
    }

    private String getArticle(List<Detail> details) {
        if (details == null || details.isEmpty()) {
            return "";
        }

        if (details.size() == 1) {
            return details.get(0).getObjet();
        } else {
            return details.stream()
                            .map(Detail::getObjet)
                            .collect(Collectors.joining(", "));
        }
    }

    public String validerRequisition(String uid, String idX3, int valeur1, int valeur2, int valeur3, String designation,int idetat,String motif) {
        String url = pythonApiUrl +"/update_demande/"; 
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("uid", uid);
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
            Optional<User> user = userRepository.findByIdx3(idX3);
            Etat etat = etatRepository.findById(idetat).orElseThrow(() -> new RuntimeException("demande non trouvé"));
            Demande demande = findByUidAndX3(uid,idX3);
            int is_da =1;
            if (user.isPresent()) {
                List<Detail> details = detailRepository.findByRefDemAndIdX3AndNotApprouve(uid, idX3);
                if(details.size() == 0 && (user.get().getInterim() != null || !user.get().getInterim().equals(" "))){
                    details=detailRepository.findByRefDemAndIdX3AndNotApprouve(uid, user.get().getInterim());
                }
                demande.setEtat(etat);
                demandeRepository.save(demande);
                String objet = getArticle(details);
                String montant = demande.getMontant();
                validationService.saveValidation(uid,objet,montant, motif,etat, user.get(),is_da);
                if (etat.getIdEtat() == 2) {
                    for (Detail detail : details) {
                        detail.setEtat(etat);
                        System.out.println("voici le uid:" + detail.getUid());
                        detailRepository.save(detail);
                        // validationService.saveValidation(detail.getUid(),detail.getObjet(),detail.getMontant(), motif, etat, user.get(), is_da);
                    }
                    String email,validateur;
                    if (user.get().getId_x3().equals("PDG")) {
                        validateur = user.get().getNom() +" "+user.get().getPrenom();
                        email = "mandresy.rakotonarivo@habibo.mg";
                        envoyeMailToGenerateBC(email, validateur, uid);
                    }
                    subject = "[BPA] Demande N°" + demande.getUid() + " - Validation DA";
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
                        + "  <h2>Demande BPA N°" + demande.getUid() + "</h2>"
                        + "</div>"
                        + "<div class='content'>"
                        + "  <p>Bonjour " + demande.getDemandeur() + ",</p>"
                        + "  <div class='highlight'>"
                        + "    <p><span class='success-badge'>VALIDATION DE LA DEMANDE</span></p>"
                        + "    <p>Nous avons le plaisir de vous informer que votre demande a été validée définitivement par <strong>"
                        + user.get().getPrenom() + " " + user.get().getNom() + "</strong> .</p>"
                        + "  </div>"
                        + "  <p>Votre demande est maintenant en attente de transformation en BC.</p>"
                        + "  <p>Cordialement,</p>"
                        + "</div>"
                        + "  <div class='footer'>"
                        + "    <p>© 2025 Plateforme d'approbation de BPA. Tous droits réservés.</p>"
                        + "    <p>Cet e-mail a été envoyé automatiquement, merci de ne pas y répondre.</p>"
                        + "  </div>"
                        + "</body>"
                        + "</html>";
                } else {
                    for (Detail detail : details) {
                        detail.setEtat(etat);
                        System.out.println("voici le uid:" + detail.getUid());
                        detailRepository.save(detail);
                    }
                    subject = "[BPA] Demande N°" + demande.getUid() + " - Notification de refus DA";

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
                        + "  <h2>Demande BPA N°" + demande.getUid() + "</h2>"
                        + "</div>"
                        + "<div class='content'>"
                        + "  <p>Bonjour " + demande.getDemandeur() + ",</p>"
                        + "  <div class='highlight'>"
                        + "    <p><span class='alert-badge'>DEMANDE REFUSÉE</span></p>"
                        + "    <p>Votre demande a été examinée et refusée par <strong>"
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
                }
                emailService.sendEmail(demande.getEmail(), subject, content);
                return response.getBody();
            }else{
                return "l'utilisateur en question n'existe pas";
            }
        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }

    public Demande findByUid(String uid){
        Demande demande = demandeRepository.findByUid(uid)
            .orElseThrow(() -> new RuntimeException("demande non trouvé"));
        return demande;
    }
    
    public Demande findByUidAndX3(String uid,String id_x3){
        Optional<Demande> demande = demandeRepository.findByUidAndIdX3(uid,id_x3);
        if (demande.isPresent()) {
            return demande.get();
        }else{
            return null;
        }
    }

    private void envoyeMailToGenerateBC(String email,String validateur,String uid){
        String subject = "[BPA] Demande d'achat N°" + uid + " - Generation du bon de commande";

        String content = "<!DOCTYPE html>"
            + "<html lang='fr'>"
            + "<head>"
            + "<meta charset='UTF-8'>"
            + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
            + "<title>Confirmation de compte</title>"
            + "<style>"
            + "  body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; }"
            + "  .container { max-width: 600px; margin: 20px auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; }"
            + "  .header { background-color: #2c3e50; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; }"
            + "  .content { padding: 20px; }"
            + "  .footer { text-align: center; padding: 20px; font-size: 12px; color: #777; }"
            + "  .button { background-color: #3498db; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block; }"
            + "  .credentials { background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 20px 0; }"
            + "  .logo { max-width: 150px; margin-bottom: 20px; }"
            + "</style>"
            + "</head>"
            + "<body>"
            + "<div class='container'>"
            + "  <div class='header'>"
            + "    <h1>Easy approve BPA</h1>"
            + "  </div>"
            + "  <div class='content'>"
            + "  <p>Bonjour,</p>"
            + "    <p>Une demande d'achat a été validée par <strong>"+validateur+"</strong> et il est de votre role de generer la bon de commande pour ce demande sur sagex3</p>"
            + "    <div class='credentials'>"
            + "     <p><strong>N° de la demande :</strong> " +uid+ "</p>"
            + "    </div>"
            + "    <p>Cordialement,</p>"
            + "  </div>"
            + "  <div class='footer'>"
            + "    <p>© 2025 Plateforme d'approbation de BPA. Tous droits réservés.</p>"
            + "    <p>Cet e-mail a été envoyé automatiquement, merci de ne pas y répondre.</p>"
            + "  </div>"
            + "</div>"
            + "</body>"
            + "</html>";
            emailService.sendEmail(email, subject, content);
    }
}
