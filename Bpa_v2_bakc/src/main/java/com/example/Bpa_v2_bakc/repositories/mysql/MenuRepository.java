package com.example.Bpa_v2_bakc.repositories.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Bpa_v2_bakc.entities.mysql.Menu;

public interface MenuRepository extends JpaRepository<Menu,Integer>{
    List<Menu> findByRoleAndActiveTrue(String role);
}
