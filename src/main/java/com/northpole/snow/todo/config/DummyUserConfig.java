package com.northpole.snow.todo.config;

import com.northpole.snow.todo.domain.Pasazer;
import com.northpole.snow.todo.domain.PasazerRepository;
import com.northpole.snow.todo.domain.Administrator;
import com.northpole.snow.todo.domain.AdministratorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DummyUserConfig {

    @Bean
    CommandLineRunner initDummyUser(PasazerRepository pasazerRepository, 
                                   AdministratorRepository administratorRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            String login = "szef";
            if (pasazerRepository.findByLogin(login).isEmpty()) {
                Pasazer pasazer = new Pasazer();
                pasazer.setImienazwisko("szef admin");
                pasazer.setEmail("szef@example.com");
                pasazer.setNumertelefonu("123456789");
                pasazer.setLogin(login);
                pasazer.setHaslo(passwordEncoder.encode("password123"));
                pasazerRepository.save(pasazer);

                // Tworzymy Administratora powiązanego z tym pasazerem
                Administrator admin = new Administrator();
                admin.setPasazerid(pasazer);
                administratorRepository.save(admin);

                System.out.println("szef admin created!");
            }
        };
    }
}
