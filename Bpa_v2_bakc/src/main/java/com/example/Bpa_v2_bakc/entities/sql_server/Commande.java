package com.example.Bpa_v2_bakc.entities.sql_server;

import com.example.Bpa_v2_bakc.entities.mysql.Etat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "commande")
public class Commande {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_commande")
    private int idCommande;

    private String uid;

    private String societe;
    private String demandeur;
    private String email;
    private String beneficiaire;
    private String id_x3;
    private String validateur;
    private String date;
    private String montant;
    private String devise;
    private String codepayement;
    private String modepayement;
    private String codeA;
    private String signature;

    @Column(name = "lien_pj")
    private String lienPj;
    private Boolean isnotif = false;

    
    @ManyToOne
    @JoinColumn(name = "etat", referencedColumnName = "id_etat")
    private Etat etat;
}
