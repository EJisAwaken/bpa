package com.example.Bpa_v2_bakc.entities.sql_server;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "detailmere")
public class Detailmere {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detail")
    private int idDetail;

    @Column(unique=true)
    private String uid;

    private String mere;
    private String code_article;
    private String quantite;
    private String pu;
    private String objet;
    private String montant;
    private String devise;
    private String fournisseur;
    private String societe;
}
