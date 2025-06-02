package com.northpole.snow.todo.service;

import com.northpole.snow.todo.domain.Pasazer;
import com.northpole.snow.todo.domain.PasazerRepository;
import com.northpole.snow.todo.domain.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import com.northpole.snow.todo.config.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PasazerRepository pasazerRepository;
    private final AdministratorRepository administratorRepository;

    @Autowired
    public CustomUserDetailsService(PasazerRepository pasazerRepository, AdministratorRepository administratorRepository) {
        this.pasazerRepository = pasazerRepository;
        this.administratorRepository = administratorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Pasazer pasazer = pasazerRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        boolean isAdmin = administratorRepository.findByPasazerid(pasazer).isPresent();
        return new CustomUserDetails(pasazer, isAdmin);
    }
}
