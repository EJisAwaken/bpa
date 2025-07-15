package com.example.Bpa_v2_bakc.repositories.mysql;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Bpa_v2_bakc.entities.mysql.Role;
import com.example.Bpa_v2_bakc.entities.mysql.User;

public interface UserRepository extends JpaRepository<User,Integer>{
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM user where active is true and (:critere IS NULL OR :critere = ''OR  CONCAT(nom, ' ', prenom,'',email) LIKE :critere)",nativeQuery = true)
    Page<User> findAll(Pageable pageable , @Param("critere") String critere);

    List<User>  findByRole(Role role);

    @Query(value = "SELECT * FROM user where id_x3=:id_x3",nativeQuery = true)
    Optional<User> findByIdx3(@Param("id_x3") String id_x3);
}
