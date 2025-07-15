package com.example.Bpa_v2_bakc.repositories.mysql;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Bpa_v2_bakc.entities.mysql.Etat;


public interface EtatRepository extends JpaRepository<Etat,Integer>{
    Optional<Etat> findByIdEtat(int idEtat);

    List<Etat> findAll();
}
