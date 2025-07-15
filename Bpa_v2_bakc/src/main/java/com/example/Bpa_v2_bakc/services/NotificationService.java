package com.example.Bpa_v2_bakc.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.Bpa_v2_bakc.entities.mysql.Etat;
import com.example.Bpa_v2_bakc.entities.mysql.Notification;
import com.example.Bpa_v2_bakc.entities.mysql.User;
import com.example.Bpa_v2_bakc.entities.sql_server.Commande;
import com.example.Bpa_v2_bakc.entities.sql_server.Demande;
import com.example.Bpa_v2_bakc.entities.sql_server.Facture;
import com.example.Bpa_v2_bakc.entities.sql_server.Facturecompett;
import com.example.Bpa_v2_bakc.repositories.mysql.NotificationRepository;
import com.example.Bpa_v2_bakc.repositories.sql_server.CommandeRepository;
import com.example.Bpa_v2_bakc.repositories.sql_server.DemandeRepository;
import com.example.Bpa_v2_bakc.repositories.sql_server.FacturecompettRepository;

@Service
public class NotificationService {
    @Autowired
    private DemandeRepository demandeRepository;
    
    @Autowired
    private CommandeRepository commandeRepository;
    
    @Autowired
    private FacturecompettRepository facturecompettRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EtatService etatService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<Notification> findNotification(String id_x3){
        int etat = 1;
        return notificationRepository.findByCode_dirAndEtat(id_x3, etat);
    }

    public List<Notification> findById_x3(String id_x3){
        List<Notification> notifs = notificationRepository.findByIdX3(id_x3);
        return notifs;
    }
    
    public List<Notification> findByIdx3AndRecusFlase(String id_x3){
        List<Notification> notifs = notificationRepository.findById_X3AndRecusFlase(id_x3);
        return notifs;
    }

    public Notification findByUidAndIdX3(String uid,String dir){
        Notification notification = notificationRepository.findByUidAndId_x3(uid, dir)
            .orElseThrow(() -> new RuntimeException("demande non trouvé"));
        return notification;
    }

    public void countByZero(String dir){
        List<Notification> notifs= findByIdx3AndRecusFlase(dir);
        for (Notification notification : notifs) {
            notification.setRecus(true);
            notificationRepository.save(notification);
        } 
    }

    public void ouvrirNotif(String uid,String dir){
        Notification notif = findByUidAndIdX3(uid, dir);
        if(!notif.getRecus()){
            notif.setRecus(true);
        }
        notif.setLus(true);
        notificationRepository.save(notif);
    }

    @Scheduled(fixedRate = 10000)
    public void sendNewNotificationDemande(){
        Etat etat= etatService.findById(1);
        List<Demande> demandes = demandeRepository.findByIsnotifFalseAndEtat(etat);
        for(Demande demande: demandes){
            String message = "Nouvelle demande d'achat: "+demande.getUid()+" de " + demande.getDemandeur() +" ,Socite: "+ demande.getSociete();
            Notification notification = new Notification();
            notification.setId_x3(demande.getId_x3());
            notification.setEtat(etat.getValue());
            notification.setMessage(message);
            notification.setUid(demande.getUid());
            notification.setRecus(false);
            notification.setLus(false);
            notification.setDate_notification(LocalDateTime.now());
            notificationRepository.save(notification);
            messagingTemplate.convertAndSend("/topic/notifications/"+ notification.getId_x3(),notification);
            demande.setIsnotif(true);
            demandeRepository.save(demande);
        }
    }


    @Scheduled(fixedRate = 10000)
    public void sendNewNotificationCommande(){
        List<Commande> commandes = commandeRepository.findByIsnotifFalse();
        for(Commande commande : commandes){
            String message = "Nouvelle bon de commande : "+commande.getUid()+" de " + commande.getDemandeur() +" ,Socite: "+ commande.getSociete();
            commande.setIsnotif(true);
            commandeRepository.save(commande);
            Notification notification = new Notification();
            notification.setId_x3(commande.getId_x3());
            notification.setEtat(commande.getEtat().getValue());
            notification.setMessage(message);
            notification.setUid(commande.getUid());
            notification.setRecus(false);
            notification.setLus(false);
            notification.setDate_notification(LocalDateTime.now());
            notificationRepository.save(notification);
            messagingTemplate.convertAndSend("/topic/notifications/"+ notification.getId_x3(),notification);
        }
    }
    
    @Scheduled(fixedRate = 10000)
    public void sendNewNotificationFacture(){
        List<Facturecompett> factures = facturecompettRepository.findByIsnotifFalse();
        for(Facturecompett commande : factures){
            commande.setIsnotif(true);
            facturecompettRepository.save(commande);
            String message = "Nouvelle facture : "+commande.getUid()+" de " + commande.getMontant()+" "+commande.getDevise() +" ,Socite: "+ commande.getSociete();
            Notification notification = new Notification();
            notification.setId_x3("admin");
            notification.setEtat(commande.getEtat().getValue());
            notification.setMessage(message);
            notification.setUid(commande.getUid());
            notification.setRecus(false);
            notification.setLus(false);
            notification.setDate_notification(LocalDateTime.now());
            notificationRepository.save(notification);
            messagingTemplate.convertAndSend("/topic/notifications/"+notification.getId_x3(),notification);
        }
    }



    @Scheduled(fixedRate = 10000)
    public void countNotification(){
        List<User> users = userService.findNotAmdin();
        for (User user : users) {
            List<Notification> notifications = findByIdx3AndRecusFlase(user.getId_x3());
            int count = notifications.size();
            messagingTemplate.convertAndSend("/topic/count/" + user.getId_x3(), count);
            List<Notification> notification = findByIdx3AndRecusFlase(user.getInterim());
            int countInterim = notification.size();
            messagingTemplate.convertAndSend("/topic/countInterim/" + user.getInterim(), countInterim);
        }
    }    
}
