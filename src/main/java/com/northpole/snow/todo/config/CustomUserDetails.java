package com.northpole.snow.todo.config;

import com.northpole.snow.todo.domain.Pasazer;
import com.northpole.snow.todo.domain.AdministratorRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class CustomUserDetails implements UserDetails {

    private final Pasazer pasazer;
    private final boolean isAdmin;

    public CustomUserDetails(Pasazer pasazer, boolean isAdmin) {
        this.pasazer = pasazer;
        this.isAdmin = isAdmin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (isAdmin) {
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return roles;
    }

    @Override
    public String getPassword() {
        return pasazer.getHaslo();
    }

    @Override
    public String getUsername() {
        return pasazer.getLogin();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public Pasazer getPasazer() {
        return pasazer;
    }
}
