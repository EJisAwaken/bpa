package com.example.Bpa_v2_bakc.entities.mysql;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "etat")
public class Etat {
    @Id 
    @Column(name = "id_etat")
    private int idEtat;

    @Column(unique=true)
    private int value;
    private String def;
}
