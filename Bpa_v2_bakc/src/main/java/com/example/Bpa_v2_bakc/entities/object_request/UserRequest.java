package com.example.Bpa_v2_bakc.entities.object_request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private int idUser;
    private String nom;
    private String prenom;
    private String email;
    private int idRole;
    private String mot_de_passe;
    private String id_x3;
    private String interim;
    private  boolean active = true ;
}
