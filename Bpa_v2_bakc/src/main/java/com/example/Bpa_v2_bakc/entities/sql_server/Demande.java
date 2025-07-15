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
@Table(name = "demande")
public class Demande {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_demande")
    private int idDemande;

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
    private String signature;
    private String codeA;

    @Column(name = "lien_pj")
    private String lienPj;
    private Boolean isnotif = false;

    
    @ManyToOne
    @JoinColumn(name = "etat", referencedColumnName = "id_etat")
    private Etat etat;
}
