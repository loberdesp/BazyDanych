package com.northpole.snow.todo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrasaRepository extends JpaRepository<Trasa, Integer> {


  @Query("""
      SELECT t FROM Trasa t
      LEFT JOIN FETCH t.przystankinatrasie pit
      LEFT JOIN FETCH pit.przystanekid
      WHERE t.numertrasy = :numertrasy
      """)
  List<Trasa> findByNumertrasy(@Param("numertrasy") Integer numertrasy);
}