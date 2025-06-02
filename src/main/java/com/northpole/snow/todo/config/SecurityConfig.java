package com.northpole.snow.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;



@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder.encode("password123"))
            .roles("USER")
            .build();
    return new InMemoryUserDetailsManager(user);
}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Enable CSRF (critical for Vaadin)
        .csrf(csrf -> csrf.disable())
        .requestCache((requestCache) -> requestCache.disable())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(    "/",
    "/login",
    "/register",
    "/css/**",
    "/js/**",
    "/images/**",
    "/VAADIN/**",
    "/frontend/**",
    "/frontend-es6/**",
    "/frontend-esm/**",
    "/webjars/**",
    "/manifest.webmanifest",
    "/sw.js",
    "/offline.html",
    "/icons/**",
    "/themes/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login") // ważne!
                .permitAll()
                .defaultSuccessUrl("/", true)
            );
            http.logout(withDefaults());
            
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
