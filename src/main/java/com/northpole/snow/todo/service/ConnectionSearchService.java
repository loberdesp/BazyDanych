package com.northpole.snow.todo.service;

import com.northpole.snow.todo.domain.Trasa;
import com.northpole.snow.todo.domain.Przystaneknatrasie;
import com.northpole.snow.todo.domain.Kurs;
import com.northpole.snow.todo.ui.view.SearchConnectionsView.Connection;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

@Service
public class ConnectionSearchService {

  private final TrasaService trasaService;

  @Autowired
  public ConnectionSearchService(TrasaService trasaService) {
    this.trasaService = trasaService;
  }

  public List<Connection> findConnections(String startStop, String endStop, LocalTime afterTime) {
    List<Connection> found = new ArrayList<>();

    for (Trasa trasa : trasaService.getAllTrasy()) {
      List<Przystaneknatrasie> przystanki = new ArrayList<>(trasa.getPrzystankinatrasie());
      przystanki.sort(Comparator.comparingInt(Przystaneknatrasie::getKolejnosc));

      int startIdx = -1, endIdx = -1;
      for (int i = 0; i < przystanki.size(); i++) {
        if (przystanki.get(i).getPrzystanekid().getNazwa().equalsIgnoreCase(startStop)) {
          startIdx = i;
        }
        if (przystanki.get(i).getPrzystanekid().getNazwa().equalsIgnoreCase(endStop)) {
          endIdx = i;
          if (startIdx != -1)
            break;
        }
      }
      if (startIdx == -1 || endIdx == -1 || startIdx >= endIdx)
        continue;

      int travelTime = 0;
      for (int i = startIdx + 1; i <= endIdx; i++) {
        Integer czas = przystanki.get(i).getCzasprzejazdu();
        if (czas != null)
          travelTime += czas;
      }

      for (Kurs kurs : trasa.getKurs()) {
        if (kurs.getGodzinastartu() == null)
          continue;
        if (afterTime != null && kurs.getGodzinastartu().isBefore(afterTime))
          continue;

        found.add(new Connection(
            String.valueOf(trasa.getNumertrasy()),
            trasa.getNazwatrasy(),
            startStop,
            endStop,
            kurs.getGodzinastartu().toString(),
            String.format("%02d:%02d", travelTime / 60, travelTime % 60)));
      }
    }
    return found;
  }
}
