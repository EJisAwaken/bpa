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
@Table(name = "facturecompett")
public class Facturecompett {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_facturecompett")
    private int idFacturecompett;

    private String uid;
    private String societe;
    private String fournisseur;
    private String date;
    private String montant;
    private String devise;
    private Boolean isnotif = false;

    @ManyToOne
    @JoinColumn(name = "etat", referencedColumnName = "id_etat")
    private Etat etat;
}
