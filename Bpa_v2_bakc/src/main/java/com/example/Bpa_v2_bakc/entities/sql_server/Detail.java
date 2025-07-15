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
@Table(name = "detail")
public class Detail {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detail")
    private int idDetail;

    @Column(unique=true)
    private String uid;
    private String ref_dem;
    private String code_article;
    private String quantite;
    private String pu;
    private String objet;
    private String montant;
    private String fournisseur;
    private String devise;
    private String id_x3;

    @ManyToOne
    @JoinColumn(name = "etat", referencedColumnName = "id_etat")
    private Etat etat;
}
