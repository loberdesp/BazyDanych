package com.northpole.snow.todo.service;

import com.northpole.snow.todo.domain.Kurs;
import com.northpole.snow.todo.domain.Przystaneknatrasie;
import com.northpole.snow.todo.domain.Trasa;
import com.northpole.snow.todo.ui.view.SearchConnectionsView.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ConnectionSearchService {

    private final TrasaService trasaService;

    @Autowired
    public ConnectionSearchService(TrasaService trasaService) {
        this.trasaService = trasaService;
    }

    public List<Connection> findConnections(String startStop,
                                            String endStop,
                                            LocalTime afterTime) {

        List<Connection> result = new ArrayList<>();

        for (Trasa trasa : trasaService.getAllTrasy()) {
            List<Przystaneknatrasie> stops =
                    trasa.getPrzystankinatrasie().stream()
                         .sorted(Comparator.comparingInt(Przystaneknatrasie::getKolejnosc))
                         .toList();

            int startIdx = findIndex(stops, startStop);
            int endIdx   = findIndex(stops, endStop);
            if (startIdx < 0 || endIdx < 0 || startIdx >= endIdx) continue;

            int travelMinutes  = sumTravelTime(stops, startIdx + 1, endIdx);
            int offsetToStart  = offsetToStart(stops, startIdx);

            for (Kurs kurs : trasa.getKurs()) {
                LocalTime kursStart = kurs.getGodzinastartu();
                if (kursStart == null) continue;

                LocalTime departure = kursStart.plusMinutes(offsetToStart);
                if (afterTime != null && departure.isBefore(afterTime)) continue;

                result.add(new Connection(
                        String.valueOf(trasa.getNumertrasy()),
                        trasa.getNazwatrasy(),
                        startStop,
                        endStop,
                        departure.toString(),
                        formatDuration(travelMinutes)
                ));
            }
        }

        result.sort(Comparator.comparing(c -> LocalTime.parse(c.getDepartureTime())));
        return result;
    }

    private static int findIndex(List<Przystaneknatrasie> stops, String name) {
        for (int i = 0; i < stops.size(); i++) {
            if (stops.get(i).getPrzystanekid().getNazwa().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    private static int sumTravelTime(List<Przystaneknatrasie> stops, int from, int to) {
        return IntStream.rangeClosed(from, to)
                        .map(i -> stops.get(i).getCzasprzejazdu() == null
                                ? 0
                                : stops.get(i).getCzasprzejazdu())
                        .sum();
    }

    private static int offsetToStart(List<Przystaneknatrasie> stops, int idx) {
        return sumTravelTime(stops, 0, idx);
    }

    private static String formatDuration(int minutes) {
        return String.format("%02d:%02d", minutes / 60, minutes % 60);
    }
}

