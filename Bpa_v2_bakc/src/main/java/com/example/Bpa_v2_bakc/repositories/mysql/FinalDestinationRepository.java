package com.example.Bpa_v2_bakc.repositories.mysql;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Bpa_v2_bakc.entities.mysql.Finaldestination;

public interface FinalDestinationRepository extends JpaRepository<Finaldestination,Integer>{
    List<Finaldestination> findByActiveTrue();

    Optional<Finaldestination> findByUidAndActiveTrue(String uid);
}
