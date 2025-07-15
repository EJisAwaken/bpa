package com.example.Bpa_v2_bakc.entities.mysql;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "id_user")
    private int idUser;

    private String nom;
    private String prenom;
    
    private String email;

    @ManyToOne 
    @JoinColumn(name = "id_role")
    private Role role;

    private String id_x3;
    private String interim;

    private String mot_de_passe;
    private Boolean active=true;

    public Collection<GrantedAuthority> getAuthorities() throws Exception {
        Collection<GrantedAuthority> authorisation = new ArrayList<GrantedAuthority>();
        return authorisation;
    }
}
