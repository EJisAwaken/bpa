package com.example.Bpa_v2_bakc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Bpa_v2_bakc.entities.mysql.Role;
import com.example.Bpa_v2_bakc.repositories.mysql.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAll(){
        return roleRepository.findByActiveIsTrue();
    }

    public Role findByRole(int id) {
        return roleRepository.findByIdRole(id).orElse(null);
    }

    public void saveRole(Role role){
        roleRepository.save(role);
    }

    public void deleteRole(int idRole){
        Optional<Role> roleOpt = roleRepository.findById(idRole);
        if (roleOpt.isPresent()) {
            Role role = roleOpt.get();
            role.setActive(false);
            roleRepository.save(role);
        }else{
            throw new RuntimeException("Role not found with id: " + idRole); 
        }
    }

    public void updateRole(Role roleUpdated){
        Optional<Role> roleOptional = roleRepository.findById(roleUpdated.getIdRole());
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            role.setUnique_role(roleUpdated.getUnique_role());
            role.setRole(roleUpdated.getRole());
            roleRepository.save(role);
        }else{
            throw new RuntimeException("Role not found with id: " + roleUpdated.getIdRole()); 
        }
    }
}
