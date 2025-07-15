package com.example.Bpa_v2_bakc.repositories.sql_server;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Bpa_v2_bakc.entities.mysql.Etat;
import com.example.Bpa_v2_bakc.entities.sql_server.Demande;
import java.util.List;



public interface DemandeRepository extends JpaRepository<Demande,Integer>{
    boolean existsByUid(String uid);

    Optional<Demande> findByUid(String uid);

    @Query(value = "SELECT * FROM demande WHERE uid=:uid And id_x3=:id_x3",nativeQuery = true)
    Optional<Demande> findByUidAndIdX3(@Param("uid") String uid,@Param("id_x3") String id_x3);

    @Query(value = "SELECT * FROM demande WHERE etat = 1 AND id_x3 = :id_x3 AND (:critere is null or :critere ='' or uid like CONCAT('%', :critere, '%')) ORDER BY id_demande DESC",nativeQuery = true)
    Page<Demande> findByidX3( Pageable pageable,@Param("id_x3") String idX3,@Param("critere") String critere);
    
    @Query(value = "SELECT * FROM demande WHERE (:critere is null or :critere ='' or CONCAT(uid) like CONCAT('%', :critere, '%')) AND (:validateur is null or :validateur ='' or CONCAT(id_x3) like CONCAT('%', :validateur, '%')) AND (:etat IS NULL OR :etat =0  OR etat = :etat)",nativeQuery = true)
    Page<Demande> findAllPage( Pageable pageable,@Param("critere") String critere,@Param("validateur") String validateur,@Param("etat") int etat);

    List<Demande> findByIsnotifFalseAndEtat(Etat etat);
}
