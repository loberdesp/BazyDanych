package com.northpole.snow.todo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrzystanekRepository extends JpaRepository<Przystanek, Integer> {
  List<Przystanek> findByNazwaContainingIgnoreCaseOrUlicaContainingIgnoreCase(String nazwa, String ulica);

  List<Przystanek> findByNazwaContainingIgnoreCase(String nazwa);

  List<Przystanek> findByUlicaContainingIgnoreCase(String ulica);

  List<Przystanek> findByNazwaIn(List<String> nazwy);

  @Query("""
          SELECT DISTINCT p FROM Przystanek p
          LEFT JOIN FETCH p.przystankinatrasie pt
          LEFT JOIN FETCH pt.trasaid t
          LEFT JOIN FETCH t.kurs
          LEFT JOIN FETCH t.przystankinatrasie tpt
          WHERE p = :przystanek
      """)
  Przystanek findWithTrasyAndKursy(@Param("przystanek") Przystanek przystanek);

  @Query("""
        SELECT DISTINCT p FROM Przystanek p
        JOIN p.przystankinatrasie pnt
        JOIN pnt.trasaid t
        WHERE t.numertrasy = :numerTrasy
      """)
  List<Przystanek> findByNumerTrasy(@Param("numerTrasy") String numerTrasy);
}