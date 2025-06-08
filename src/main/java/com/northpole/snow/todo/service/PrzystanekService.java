package com.northpole.snow.todo.service;

import com.northpole.snow.todo.domain.Przystanek;
import com.northpole.snow.todo.domain.PrzystanekRepository;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrzystanekService {
  private final PrzystanekRepository przystanekRepository;

  public PrzystanekService(PrzystanekRepository przystanekRepository) {
    this.przystanekRepository = przystanekRepository;
  }

  public List<Przystanek> search(String nazwa, String ulica) {
    boolean nazwaOk = nazwa != null && !nazwa.isBlank();
    boolean ulicaOk = ulica != null && !ulica.isBlank();

    if (nazwaOk && ulicaOk) {
      return przystanekRepository.findByNazwaContainingIgnoreCaseOrUlicaContainingIgnoreCase(nazwa, ulica);
    } else if (nazwaOk) {
      return przystanekRepository.findByNazwaContainingIgnoreCase(nazwa);
    } else if (ulicaOk) {
      return przystanekRepository.findByUlicaContainingIgnoreCase(ulica);
    } else {
      return List.of(); // pusta lista, gdy oba pola puste
    }
  }

  public List<Przystanek> findAll() {
    return przystanekRepository.findAll();
  }

  public List<Przystanek> findByLineNumber(String numerTrasy) {
    return przystanekRepository.findByNumerTrasy(numerTrasy);
  }

  public void deleteById(Integer id) {
    // Najpierw usuń powiązania z Przystaneknatrasie
    Przystanek przystanek = przystanekRepository.findById(id).orElse(null);
    if (przystanek != null && przystanek.getPrzystankinatrasie() != null) {
      przystanek.getPrzystankinatrasie().clear();
      przystanekRepository.save(przystanek);
    }
    przystanekRepository.deleteById(id);
  }

  public static class TrasaGodzinaDTO {
    public final String nazwaTrasy;
    public final Integer numerTrasy;
    public final LocalTime godzinaStartu;

    public TrasaGodzinaDTO(String nazwaTrasy, Integer numerTrasy, LocalTime godzinaStartu) {
      this.nazwaTrasy = nazwaTrasy;
      this.numerTrasy = numerTrasy;
      this.godzinaStartu = godzinaStartu;
    }
  }

  public List<TrasaGodzinaDTO> getTrasyIGodziny(Przystanek przystanek) {
    Przystanek pelnyPrzystanek = przystanekRepository.findWithTrasyAndKursy(przystanek);
    List<TrasaGodzinaDTO> wynik = new ArrayList<>();
    if (pelnyPrzystanek == null || pelnyPrzystanek.getPrzystankinatrasie() == null) {
      return wynik;
    }
    for (var pnt : pelnyPrzystanek.getPrzystankinatrasie()) {
      var trasa = pnt.getTrasaid();
      if (trasa == null || trasa.getKurs() == null || trasa.getPrzystankinatrasie() == null) {
        continue;
      }
      // Posortuj przystanki na trasie po kolejnosci
      var przystankiNaTrasie = new ArrayList<>(trasa.getPrzystankinatrasie());
      przystankiNaTrasie.sort((a, b) -> Integer.compare(a.getKolejnosc(), b.getKolejnosc()));

      // Wyznacz sumę czasów przejazdu do bieżącego przystanku WŁĄCZNIE z nim
      int sumaCzasow = 0;
      for (var przystanekNaTrasie : przystankiNaTrasie) {
        sumaCzasow += przystanekNaTrasie.getCzasprzejazdu() != null ? przystanekNaTrasie.getCzasprzejazdu() : 0;
        if (przystanekNaTrasie.getId().equals(pnt.getId())) {
          break;
        }
      }

      for (var kurs : trasa.getKurs()) {
        if (kurs.getGodzinastartu() == null)
          continue;
        wynik.add(new TrasaGodzinaDTO(
            trasa.getNazwatrasy(),
            trasa.getNumertrasy(),
            kurs.getGodzinastartu().plusMinutes(sumaCzasow)));
      }
    }
    return wynik;
  }

  public List<Przystanek> searchByAll(String nazwa, String ulica, String numerTrasy) {
    boolean nazwaOk = nazwa != null && !nazwa.isBlank();
    boolean ulicaOk = ulica != null && !ulica.isBlank();
    boolean numerOk = numerTrasy != null && !numerTrasy.isBlank();

    if (!numerOk) {
      return search(nazwa, ulica);
    }

    // Pobierz przystanki na danej linii
    List<Przystanek> przystanki = przystanekRepository.findByNumerTrasy(numerTrasy);

    // Filtrowanie po nazwie i ulicy jeśli podane
    return przystanki.stream()
        .filter(p -> !nazwaOk || p.getNazwa().toLowerCase().contains(nazwa.toLowerCase()))
        .filter(p -> !ulicaOk || p.getUlica().toLowerCase().contains(ulica.toLowerCase()))
        .toList();
  }


  public List<LocalTime> getGodzinyDlaTrasy(Przystanek selected, Integer numerTrasy) {
    // Pobierz wszystkie DTO dla przystanku
    List<TrasaGodzinaDTO> trasy = getTrasyIGodziny(selected);
    // Filtrowanie po numerze trasy i zbieranie godzin (pomijamy null)
    return trasy.stream()
        .filter(dto -> numerTrasy != null && numerTrasy.equals(dto.numerTrasy) && dto.godzinaStartu != null)
        .map(dto -> dto.godzinaStartu)
        .collect(Collectors.toList());
  }

  @Transactional
  public Przystanek save(Przystanek przystanek) {
  return przystanekRepository.save(przystanek);
}
}
