package com.example.Bpa_v2_bakc.entities.mysql;
import java.time.LocalDateTime;

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
@Table(name = "validation")
public class Validation {
    @Id
    @Column(name = "id_validation")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idValidation;

    @ManyToOne
    @JoinColumn(name = "id_etat", referencedColumnName = "id_etat")
    private Etat etat;

    private String demande;
    private String objet;
    private String montant;
    
    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private User user;

    private String motif;
    private int is_da = 0;

    @Column(name = "date_validation", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime date_validation;

    private Boolean active = true;
}
