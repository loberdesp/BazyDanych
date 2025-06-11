package com.northpole.snow.todo.service;

import com.northpole.snow.todo.domain.Administrator;
import com.northpole.snow.todo.domain.AdministratorRepository;
import com.northpole.snow.todo.domain.Pasazer;
import com.northpole.snow.todo.domain.PasazerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PasazerService {

    private static final Logger logger = LoggerFactory.getLogger(PasazerService.class);

    private final PasazerRepository pasazerRepository;
    private final AdministratorRepository administratorRepository;  // <-- dodaj pole
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasazerService(PasazerRepository pasazerRepository,
                          AdministratorRepository administratorRepository, // <-- dodaj do konstruktora
                          PasswordEncoder passwordEncoder) {
        this.pasazerRepository = pasazerRepository;
        this.administratorRepository = administratorRepository;  // <-- przypisz tutaj
        this.passwordEncoder = passwordEncoder;
    }

    public boolean changePassword(String login, String oldPassword, String newPassword) {
        Optional<Pasazer> optionalPasazer = pasazerRepository.findByLogin(login);
        if (optionalPasazer.isPresent()) {
            Pasazer pasazer = optionalPasazer.get();

            logger.info("Stored hash: {}", pasazer.getHaslo());
            logger.info("Old password input: {}", oldPassword);

            boolean matches = passwordEncoder.matches(oldPassword, pasazer.getHaslo());
            logger.info("Password matches? {}", matches);

            if (!matches) {
                logger.warn("Old password does not match for login: {}", login);
                return false;
            }

            pasazer.setHaslo(passwordEncoder.encode(newPassword));
            pasazerRepository.save(pasazer);
            return true;
        } else {
            logger.warn("Pasazer with login {} not found", login);
        }

        return false;
    }

    public boolean promoteToAdmin(Integer pasazerId) {
        Optional<Pasazer> pasazerOpt = pasazerRepository.findById(pasazerId);
        if (pasazerOpt.isPresent()) {
            Pasazer pasazer = pasazerOpt.get();

            // sprawdź czy już jest adminem
            boolean isAdmin = administratorRepository.existsByPasazerid(pasazer);
            if (isAdmin) {
                return false;
            }

            Administrator admin = new Administrator();
            admin.setPasazerid(pasazer);
            administratorRepository.save(admin);
            return true;
        }
        return false;
    }
}
