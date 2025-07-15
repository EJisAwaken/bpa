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
@Table(name = "role")
public class Role {
    @Id 
    @Column(name = "id_role")
    private int idRole;

    @Column(unique=true)
    private String unique_role;
    private String role;
    private Boolean active = true;
}