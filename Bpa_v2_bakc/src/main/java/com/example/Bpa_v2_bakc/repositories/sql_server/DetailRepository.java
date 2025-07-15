package com.example.Bpa_v2_bakc.repositories.sql_server;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Bpa_v2_bakc.entities.sql_server.Detail;

public interface DetailRepository extends JpaRepository<Detail,Integer>{
    @Query(value = "SELECT * FROM detail WHERE  ref_dem=:uid And id_x3=:id_x3",nativeQuery = true)
    List<Detail> findByRefDemAndIdX3(@Param("uid") String uid,@Param("id_x3") String id_x3);
    
    @Query(value = "SELECT * FROM detail WHERE etat = 1 and ref_dem=:uid And id_x3=:id_x3",nativeQuery = true)
    List<Detail> findByRefDemAndIdX3AndNotApprouve(@Param("uid") String uid,@Param("id_x3") String id_x3);
    
    
    @Query(value = "SELECT * FROM detail WHERE  ref_dem=:uid  And etat=:idetat",nativeQuery = true)
    List<Detail> findByRefDemAndEtat(@Param("uid") String uid,@Param("idetat") int idetat);
    
    @Query(value = "SELECT * FROM detail WHERE ref_dem=:uid",nativeQuery = true)
    List<Detail> findByRefDem(@Param("uid") String uid);

    boolean existsByUid(String uid);

    Optional<Detail> findByUid(String uid);
}
