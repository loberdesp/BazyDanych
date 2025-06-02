package com.northpole.snow.todo.config;

import com.northpole.snow.todo.domain.AdministratorRepository;
import com.northpole.snow.todo.domain.PasazerRepository;
import com.northpole.snow.todo.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.core.userdetails.User;


@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomUserDetailsService userDetailsService(
            PasazerRepository pasazerRepository,
            AdministratorRepository administratorRepository) {
        return new CustomUserDetailsService(pasazerRepository, administratorRepository);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   DaoAuthenticationProvider authProvider) throws Exception {

        http.authenticationProvider(authProvider)
            .csrf(csrf -> csrf.disable())
            .requestCache(requestCache -> requestCache.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/login", "/register",
                    "/css/**", "/js/**", "/images/**",
                    "/VAADIN/**", "/frontend/**", "/frontend-es6/**", "/frontend-esm/**",
                    "/webjars/**", "/manifest.webmanifest", "/sw.js", "/offline.html",
                    "/icons/**", "/themes/**"
                ).permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .permitAll()
                .defaultSuccessUrl("/", true)
            )
            .logout(logout -> logout.logoutSuccessUrl("/login?logout"));

        return http.build();
    }
}
