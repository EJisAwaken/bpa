package com.example.Bpa_v2_bakc.repositories.mysql;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Bpa_v2_bakc.entities.mysql.Role;

public interface RoleRepository extends JpaRepository<Role,Integer>{
    List<Role> findByActiveIsTrue();
    Optional<Role> findByIdRole(int idRole);
}
