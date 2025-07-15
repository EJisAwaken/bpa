package com.example.Bpa_v2_bakc.repositories.sql_server;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Bpa_v2_bakc.entities.sql_server.Facturecompett;

public interface FacturecompettRepository extends JpaRepository<Facturecompett,Integer>{
    boolean existsByUid(String uid);

    @Query(value = "SELECT * FROM facturecompett WHERE etat = 1  AND (:critere is null or :critere ='' or uid like CONCAT('%', :critere, '%'))",nativeQuery = true)
    Page<Facturecompett> findAllPage( Pageable pageable,@Param("critere") String critere);

    @Query(value = "SELECT * FROM facturecompett WHERE (:etat IS NULL OR :etat =0  OR etat = :etat)  AND (:critere is null or :critere ='' or uid like CONCAT('%', :critere, '%'))",nativeQuery = true)
    Page<Facturecompett> findAllForLecPage( Pageable pageable,@Param("critere") String critere,@Param("etat") int etat);

    Optional<Facturecompett> findByUid(String uid);

    List<Facturecompett> findByIsnotifFalse();
}