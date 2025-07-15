package com.example.Bpa_v2_bakc.util;

import java.util.Date;
import java.util.LinkedHashMap;

import com.example.Bpa_v2_bakc.entities.mysql.Role;
import com.example.Bpa_v2_bakc.entities.mysql.User;

import io.jsonwebtoken.*;

public class JwtUtil {
    private static final String SECRET = "validationBPA012025";
    private static final long EXPIRATION_TIME = 14_400_000; // 20min
    public static String generateToken(User user) {
        String token = Jwts.builder()
            .claim("user", user)
            .setSubject(user.getEmail())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS256, SECRET)
            .compact();
    
        // System.out.println("Generated Token: " + token);
        return token;
    }
    

    public static User getUser(String token){
        User user = null ;
        Claims cls = extractUsername(token);
        if(cls.containsKey("user")){
            LinkedHashMap<String, Object> userClaims = (LinkedHashMap<String, Object>) cls.get("user");
            user = new User();
            user.setIdUser((Integer)userClaims.get("idUser"));
            user.setNom((String)userClaims.get("nom"));
            user.setPrenom((String)userClaims.get("prenom"));
            user.setEmail((String)userClaims.get("email"));
            if(userClaims.containsKey("role")){
            LinkedHashMap<String,Object> RoleClaims = (LinkedHashMap<String, Object>) userClaims.get("role");
                Role role = new Role();
                role.setIdRole((int) RoleClaims.get("idRole"));
                role.setUnique_role((String) RoleClaims.get("unique_role"));
                role.setRole((String) RoleClaims.get("role"));
                user.setRole(role);
            }
            user.setId_x3((String) userClaims.get("id_x3"));
            user.setInterim((String) userClaims.get("interim"));
            user.setMot_de_passe((String) userClaims.get("mot_de_passe"));
        }
        return user;
    }
    
    public static Claims extractUsername(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        } catch (JwtException e) {
            System.out.println("Invalid JWT Token: " + e.getMessage());
            return null;
        }
    }    
}
