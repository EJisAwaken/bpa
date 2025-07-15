package com.example.Bpa_v2_bakc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Bpa_v2_bakc.entities.model.IsLogged;
import com.example.Bpa_v2_bakc.entities.mysql.User;
import com.example.Bpa_v2_bakc.repositories.mysql.UserRepository;
import com.example.Bpa_v2_bakc.util.JwtUtil;

@Service
public class AuthService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public IsLogged authenticate(String email,String password){
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        boolean isTrue = passwordEncoder.matches(password, user.getMot_de_passe());    
        IsLogged isLogged = new IsLogged();
        if (isTrue) {
            String token = JwtUtil.generateToken(user);
            isLogged.setToken(token);
            isLogged.setRole(user.getRole().getUnique_role());
            System.out.println("connexion reuissie: " + isLogged);
            return isLogged;
        }else{
            System.out.println("erreur lors de la connexion");
            return null;
        }
    } 
}
