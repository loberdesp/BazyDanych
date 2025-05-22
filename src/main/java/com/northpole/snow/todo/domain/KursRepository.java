package com.northpole.snow.todo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KursRepository extends JpaRepository<Kurs, Integer> {
  @Query("SELECT k FROM Kurs k JOIN FETCH k.trasaid t")
  List<Kurs> findAllWithTrasa();
}