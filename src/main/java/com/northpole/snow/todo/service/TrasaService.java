package com.northpole.snow.todo.service;

import com.northpole.snow.todo.domain.Trasa;
import com.northpole.snow.todo.domain.TrasaRepository;
import com.northpole.snow.todo.domain.Przystaneknatrasie;
import com.northpole.snow.todo.domain.Przystanek;
import com.northpole.snow.todo.domain.PrzystanekRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrasaService {

  private final TrasaRepository trasaRepository;
  private final PrzystanekRepository przystanekRepository;

  public TrasaService(TrasaRepository trasaRepository, PrzystanekRepository przystanekRepository) {
    this.trasaRepository = trasaRepository;
    this.przystanekRepository = przystanekRepository;
  }

  // Zwraca listę wszystkich linii w formacie "Linia X"
  public List<String> getAllLines() {
    // joiny przez @EntityGraph w repozytorium
    return trasaRepository.findAll().stream()
        .map(trasa -> "Linia " + trasa.getNumertrasy())
        .distinct()
        .collect(Collectors.toList());
  }

  // Zwraca listę nazw przystanków dla wybranej linii (po numerze trasy)
  public List<String> getStopsForLine(String line) {
    if (line == null || !line.startsWith("Linia "))
      return Collections.emptyList();
    try {
      int numerTrasy = Integer.parseInt(line.replace("Linia ", ""));
      // joiny przez @EntityGraph w repozytorium
      List<Trasa> trasy = trasaRepository.findByNumertrasy(numerTrasy);
      return trasy.stream()
          .flatMap(trasa -> trasa.getPrzystankinatrasie().stream())
          .sorted(Comparator.comparingInt(Przystaneknatrasie::getKolejnosc))
          .map(przystanekNaTrasie -> przystanekNaTrasie.getPrzystanekid().getNazwa())
          .distinct()
          .collect(Collectors.toList());
    } catch (NumberFormatException e) {
      return Collections.emptyList();
    }
  }

  // Zwraca listę czasów kumulujących od wybranego przystanku początkowego dla
  // danej linii
  public List<Integer> getTimesForLine(String line, String startStop) {
    if (line == null || !line.startsWith("Linia ") || startStop == null)
      return Collections.emptyList();
    try {
      int numerTrasy = Integer.parseInt(line.replace("Linia ", ""));
      List<Trasa> trasy = trasaRepository.findByNumertrasy(numerTrasy);
      List<Przystaneknatrasie> przystanki = trasy.stream()
          .flatMap(trasa -> trasa.getPrzystankinatrasie().stream())
          .sorted(Comparator.comparingInt(Przystaneknatrasie::getKolejnosc))
          .collect(Collectors.toList());

      // znajdź indeks przystanku początkowego
      int startIdx = -1;
      for (int i = 0; i < przystanki.size(); i++) {
        if (startStop.equals(przystanki.get(i).getPrzystanekid().getNazwa())) {
          startIdx = i;
          break;
        }
      }
      if (startIdx == -1)
        return Collections.emptyList();

      List<Integer> times = new ArrayList<>();
      int sum = 0;
      for (int i = startIdx; i < przystanki.size(); i++) {
        if (i == startIdx) {
          times.add(0); // pierwszy przystanek (startowy) zawsze 0
        } else {
          Integer czas = przystanki.get(i).getCzasprzejazdu();
          Integer czasPoprzedni = przystanki.get(i - 1).getCzasprzejazdu();
          if (czas != null && czasPoprzedni != null) {
            sum += (czas);
            times.add(sum);
          } else {
            times.add(null);
          }
        }
      }
      return times;
    } catch (NumberFormatException e) {
      return Collections.emptyList();
    }
  }

  // Zwraca nazwę trasy dla podanego numeru linii (np. "Linia 5")
  public String getRouteNameForLine(String line) {
    if (line == null || !line.startsWith("Linia "))
      return "";
    try {
      int numerTrasy = Integer.parseInt(line.replace("Linia ", ""));
      List<Trasa> trasy = trasaRepository.findByNumertrasy(numerTrasy);
      if (!trasy.isEmpty()) {
        // Zakładamy, że nazwa trasy jest taka sama dla wszystkich tras o tym numerze
        return trasy.get(0).getNazwatrasy();
      }
      return "";
    } catch (NumberFormatException e) {
      return "";
    }
  }

  // Pobierz wszystkie przystanki z bazy (unikalne nazwy)
  public List<String> getAllStopsFromDb() {
    return przystanekRepository.findAll().stream()
        .map(Przystanek::getNazwa)
        .distinct()
        .sorted()
        .collect(Collectors.toList());
  }

  // Dodaj nową trasę z przystankami na trasie
  public boolean addNewRouteWithStops(String numer, String nazwa, List<String> przystanki, List<Integer> czasy) {
    try {
      List<Przystanek> przystankiEntities = przystanekRepository.findByNazwaIn(przystanki);
      if (przystankiEntities.size() != przystanki.size())
        return false;

      Trasa trasa = new Trasa();
      trasa.setNumertrasy(Integer.parseInt(numer));
      trasa.setNazwatrasy(nazwa);
      trasa.setKierunek((short) 1); // lub inny domyślny kierunek

      Set<Przystaneknatrasie> przystankiNaTrasie = new LinkedHashSet<>();
      for (int i = 0; i < przystankiEntities.size(); i++) {
        Przystaneknatrasie pnt = new Przystaneknatrasie();
        pnt.setTrasaid(trasa);
        pnt.setPrzystanekid(przystankiEntities.get(i));
        pnt.setKolejnosc(i + 1);
        pnt.setCzasprzejazdu(czasy.get(i));
        przystankiNaTrasie.add(pnt);
      }
      trasa.setPrzystankinatrasie(przystankiNaTrasie);

      trasaRepository.save(trasa); // zapisuje trasę oraz przystanki na trasie dzięki kaskadzie

      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
