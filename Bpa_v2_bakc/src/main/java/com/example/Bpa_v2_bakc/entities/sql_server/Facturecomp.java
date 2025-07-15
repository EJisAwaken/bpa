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
@Table(name = "facturecomp")
public class Facturecomp {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_facturecomp")
    private int idFacturecomp;

    private String uid;
    
    private String reffc;

    private String mere;
    private String societe;
    private String fournisseur;
    private String code;
    private String objet;
    private String date;
    private String montant;
    private String devise;
}
