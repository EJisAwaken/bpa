package com.example.Bpa_v2_bakc.filter;


import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.Bpa_v2_bakc.entities.mysql.User;
import com.example.Bpa_v2_bakc.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterToken extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        this.verifyToken(request);
        filterChain.doFilter(request, response);
    }

    public String getBearerToken(HttpServletRequest request) {
        String bearerToken = null;
        String authorization = request.getHeader("Authorization");
        if ( authorization!=null && !authorization.isEmpty() && authorization.startsWith("Bearer ")) {
            bearerToken = authorization.substring(7);
        }
        return bearerToken;
    }   

    private void verifyToken(HttpServletRequest request)  {
        String token = this.getBearerToken(request);
        // System.out.println("The big token is ==> " + token);
        try {
            User user =  JwtUtil.getUser(token);
            if(user != null) {
                UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getMot_de_passe())
                    .authorities(user.getAuthorities()) 
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getAuthorities()); 
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // System.out.println("Efa ato ve enaooo???");
            }else{
                throw new Exception("tsy ato aona enao????");
            }
        } catch (Exception e) {
            System.out.println("Filter Error: " + e.getMessage());
        }
    }   
}
