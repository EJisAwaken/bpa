package com.example.Bpa_v2_bakc.repositories.sql_server;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Bpa_v2_bakc.entities.sql_server.Facture;


public interface FactureRepository extends JpaRepository<Facture,Integer>{
    boolean existsByUid(String uid);

    @Query(value = "SELECT * FROM facture WHERE :critere is null or :critere ='' or uid like CONCAT('%', :critere, '%')",nativeQuery = true)
    Page<Facture> findAllPage( Pageable pageable,@Param("critere") String critere);

    Optional<Facture> findByUid(String uid);
}
