package com.example.Bpa_v2_bakc.repositories.mysql;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Bpa_v2_bakc.entities.mysql.Etat;
import com.example.Bpa_v2_bakc.entities.mysql.User;
import com.example.Bpa_v2_bakc.entities.mysql.Validation;

public interface ValidationRepository extends JpaRepository<Validation,Integer>{
    @Query(value ="select * from validation where (:idUser IS NULL OR :idUser =0  OR id_user = :idUser) and active is true and DATE(date_validation) BETWEEN :date1 AND :date2 ORDER BY id_validation DESC",nativeQuery = true)
    Page<Validation> findAllValidation(Pageable pageable,@Param("date1") String date1, @Param("date2") String date2,@Param("idUser") int idUser);

    @Query(value ="select * from validation where active is true and id_user=:user and DATE(date_validation) BETWEEN :date1 AND :date2 ORDER BY id_validation DESC",nativeQuery = true)
    Page<Validation> findByIdUser(@Param("user") int user, Pageable pageable,@Param("date1") String date1, @Param("date2") String date2);


    Optional<Validation> findByDemandeAndUser(String demande,User user);
    
    @Query(value = "SELECT * FROM validation WHERE is_exported is false and id_etat = 1 or id_etat = 3 or id_etat = 5 or id_etat = 7", nativeQuery = true)
    List<Validation> findRejeterForX3();
    
    @Query(value = "SELECT * FROM validation WHERE is_exported is false  and id_etat = 8", nativeQuery = true)
    List<Validation> findValiderForX3();

    boolean existsByDemandeAndUserAndEtat(String demande,User user,Etat etat);
}
