package com.northpole.snow.todo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
    Optional<Administrator> findByPasazerid(Pasazer pasazer);
    boolean existsByPasazerid(Pasazer pasazer);
}