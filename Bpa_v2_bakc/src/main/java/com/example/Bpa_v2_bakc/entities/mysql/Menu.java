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
@Table(name = "menu")
public class Menu {
    @Id 
    @Column(name = "id_menu")
    private int idMenu;

    private String role;
    private String label;
    private String icone;
    private String route;
    private Boolean active = true;
}
