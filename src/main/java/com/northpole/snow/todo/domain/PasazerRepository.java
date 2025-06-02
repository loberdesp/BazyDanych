package com.northpole.snow.todo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasazerRepository extends JpaRepository<Pasazer, Integer> {
    Optional<Pasazer> findByEmail(String email);
    Optional<Pasazer> findByLogin(String login);
  }