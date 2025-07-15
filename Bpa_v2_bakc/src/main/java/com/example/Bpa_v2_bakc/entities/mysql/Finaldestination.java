package com.example.Bpa_v2_bakc.entities.mysql;

import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Setter
@Getter
@Entity
@Table(name = "finaldestination")
public class Finaldestination {
    @Id
    @Column(name = "id_final")
    private int idFinal;

    private String uid;
    private String designation;
    private Boolean active = true ;
}
