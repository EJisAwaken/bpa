package com.example.Bpa_v2_bakc.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bpa_v2_bakc.entities.mysql.Role;
import com.example.Bpa_v2_bakc.services.RoleService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;





@RestController
@RequestMapping("/api/role")
@CrossOrigin("*")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping()
    public ResponseEntity<List<Role>> getAll(){
        List<Role> roles = roleService.getAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getById(@PathVariable int id) {
        Role role = roleService.findByRole(id);
        return ResponseEntity.ok(role);
    }
    

    @PostMapping()
    public void insertRole(@RequestBody Role role) {
        roleService.saveRole(role);
    }

    @PutMapping("/{id}")
    public void deleteRole(@PathVariable int id) {
        roleService.deleteRole(id);
    }

    @PutMapping()
    public void putMethodName(@RequestBody Role role) {
        roleService.updateRole(role);
    }
    
}
