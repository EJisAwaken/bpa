package com.example.Bpa_v2_bakc.repositories.sql_server;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.Bpa_v2_bakc.entities.sql_server.Facturecomp;

public interface FacturecompRepository extends JpaRepository<Facturecomp,Integer>{
    // boolean existsByUid(String uid);
    boolean existsByReffcAndMereAndCode(String reffc,String mere,String code);

    List<Facturecomp> findByMere(String mere);

    @Query(value = "SELECT * FROM facturecomp WHERE mere=:mere",nativeQuery = true)
    List<Facturecomp> findByMereAndEtat(String mere);

    // Optional<Facturecomp> findByUid(String uid);
    
    List<Facturecomp> findAllByReffc(String uid);


    // Page<Facturecomp> findAllPage(Pageable page,@Param("critere") String critere);
}
