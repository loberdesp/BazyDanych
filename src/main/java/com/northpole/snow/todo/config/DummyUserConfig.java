package com.northpole.snow.todo.config;

import com.northpole.snow.todo.domain.Pasazer;
import com.northpole.snow.todo.domain.PasazerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DummyUserConfig {

    @Bean
    CommandLineRunner initDummyUser(PasazerRepository pasazerRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String login = "dummy";
            if (pasazerRepository.findByLogin(login).isEmpty()) {
                Pasazer pasazer = new Pasazer();
                pasazer.setImienazwisko("Dummy User");
                pasazer.setEmail("dummy@example.com");
                pasazer.setNumertelefonu("123456789");
                pasazer.setLogin(login);
                pasazer.setHaslo(passwordEncoder.encode("password123"));
                pasazerRepository.save(pasazer);
                System.out.println("Dummy user created!");
            }
        };
    }
}
