package com.example.Bpa_v2_bakc.repositories.sql_server;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Bpa_v2_bakc.entities.sql_server.Commande;

public interface CommandeRepository extends JpaRepository<Commande,Integer>{
    @Query(value = "SELECT * FROM commande WHERE uid=:uid AND id_x3 = :id_x3_1 ",nativeQuery = true)
    Optional<Commande> findByUidAndId_x3(String uid,@Param("id_x3_1") String idX31);
    
    @Query(value = "SELECT * FROM commande WHERE uid=:uid",nativeQuery = true)
    List<Commande> findByUid(String uid);
    
    @Query(value = "SELECT * FROM commande WHERE uid=:uid AND id_x3 = :id_x3",nativeQuery = true)
    List<Commande> findByUidAndIdX3(@Param("uid") String uid,@Param("id_x3")String id_x3);
    
    @Query(value = "SELECT * FROM commande WHERE uid=:uid AND id_x3!= :id_x3",nativeQuery = true)
    List<Commande> findByUidNoIdX3(@Param("uid") String uid,@Param("id_x3")String id_x3);
    
    @Query(value = "SELECT * FROM commande WHERE uid=:uid AND id_x3= :id_x3",nativeQuery = true)
    Optional<Commande> findByUidIdX3(@Param("uid") String uid,@Param("id_x3")String id_x3);

    @Query(value = "SELECT * FROM commande WHERE (etat = 1 OR etat =4)  AND id_x3 = :id_x3 AND (:critere is null or :critere ='' or uid like CONCAT(:critere)) ORDER BY id_commande DESC",nativeQuery = true)
    Page<Commande> findByidX3( Pageable pageable,@Param("id_x3") String idX3,@Param("critere") String critere);
    
    @Query(value = "SELECT * FROM commande WHERE (:critere is null or :critere ='' or CONCAT(uid) like CONCAT('%', :critere, '%')) AND (:validateur is null or :validateur ='' or CONCAT(id_x3) like CONCAT('%', :validateur, '%')) AND (:etat IS NULL OR :etat =0  OR etat = :etat)",nativeQuery = true)
    Page<Commande> findAllPage( Pageable pageable,@Param("critere") String critere,@Param("validateur") String validateur,@Param("etat") int etat);

    List<Commande> findByIsnotifFalse();

}
