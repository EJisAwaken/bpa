package com.example.Bpa_v2_bakc.repositories.sql_server;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.Bpa_v2_bakc.entities.sql_server.Detailmere;

public interface DetailmereRepository extends JpaRepository<Detailmere,Integer>{
    boolean existsByUid(String uid);

    @Query(value = "SELECT * FROM detailmere WHERE mere=:mere",nativeQuery = true)
    List<Detailmere> findByMere(String mere);
}
