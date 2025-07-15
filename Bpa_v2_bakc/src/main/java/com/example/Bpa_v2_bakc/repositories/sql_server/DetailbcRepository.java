package com.example.Bpa_v2_bakc.repositories.sql_server;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Bpa_v2_bakc.entities.sql_server.Detailbc;

public interface DetailbcRepository extends JpaRepository<Detailbc,Integer>{
    boolean existsByUid(String uid);

    @Query(value = "SELECT * FROM detailbc WHERE ref_dem=:uid",nativeQuery = true)
    List<Detailbc> findByRefDem(@Param("uid") String uid);
}
