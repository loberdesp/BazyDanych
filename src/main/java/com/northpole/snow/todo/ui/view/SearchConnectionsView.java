package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import com.northpole.snow.todo.domain.Trasa;
import com.northpole.snow.todo.domain.Przystaneknatrasie;
import com.northpole.snow.todo.domain.Kurs;
import com.northpole.snow.todo.service.TrasaService;
import com.northpole.snow.todo.service.PrzystanekService;
import com.northpole.snow.todo.service.KursService;
import com.northpole.snow.todo.service.ConnectionSearchService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import com.vaadin.flow.component.combobox.ComboBox;

@PageTitle("Wyszukaj połączenia")
@Route("wyszukaj-polaczenia")
@RolesAllowed("USER")
@Menu(order = 7, title = "Wyszukaj połączenia", icon = "vaadin:search")
@AnonymousAllowed
public class SearchConnectionsView extends Main {

  private final TrasaService trasaService;
  private final KursService kursService;
  private final ConnectionSearchService connectionSearchService;

  @Autowired
  public SearchConnectionsView(TrasaService trasaService, KursService kursService,
      ConnectionSearchService connectionSearchService) {
    this.trasaService = trasaService;
    this.kursService = kursService;
    this.connectionSearchService = connectionSearchService;

    // Pobierz wszystkie przystanki z bazy
    List<String> allStops = trasaService.getAllStopsFromDb();

    ComboBox<String> startStopField = new ComboBox<>("Przystanek początkowy");
    startStopField.setItems(allStops);
    startStopField.setAllowCustomValue(false);
    startStopField.setWidth("200px");

    ComboBox<String> endStopField = new ComboBox<>("Przystanek docelowy");
    endStopField.setItems(allStops);
    endStopField.setAllowCustomValue(false);
    endStopField.setWidth("200px");

    TimePicker departureTimeField = new TimePicker("Godzina odjazdu");
    departureTimeField.setWidth("200px");
    departureTimeField.setLocale(new java.util.Locale("pl"));
    departureTimeField.setStep(java.time.Duration.ofMinutes(30));

    Button searchButton = new Button("Szukaj");
    searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    Grid<Connection> connectionGrid = new Grid<>(Connection.class, false);

    Button resetButton = new Button("Resetuj", e -> {
      startStopField.clear();
      endStopField.clear();
      departureTimeField.clear();
      connectionGrid.setItems(new ArrayList<>()); // resetuj tabelę
    });

    HorizontalLayout buttonLayout = new HorizontalLayout(searchButton, resetButton);

    connectionGrid.addColumn(Connection::getStartStop).setHeader("Przystanek początkowy");
    connectionGrid.addColumn(Connection::getEndStop).setHeader("Przystanek docelowy");
    connectionGrid.addColumn(Connection::getLineNumber).setHeader("Nr linii");
    connectionGrid.addColumn(Connection::getLineName).setHeader("Nazwa linii");
    connectionGrid.addColumn(Connection::getDepartureTime).setHeader("Czas odjazdu");
    connectionGrid.addColumn(Connection::getTravelTime).setHeader("Czas podróży");
    connectionGrid.setWidthFull();
    connectionGrid.setHeight("300px");

    connectionGrid.setItems(new ArrayList<>()); // domyślnie pusto

    searchButton.addClickListener(e -> {
      String startStop = startStopField.getValue();
      String endStop = endStopField.getValue();
      var afterTime = departureTimeField.getValue();

      if (startStop == null || endStop == null || startStop.isBlank() || endStop.isBlank()) {
        Notification.show("Podaj oba przystanki!", 3000, Notification.Position.MIDDLE);
        return;
      }

      // Przelicz godzinę odjazdu jako czas startu kursu + suma czasów przejazdu do
      // przystanku początkowego
      List<Connection> found = connectionSearchService.findConnections(startStop, endStop, afterTime).stream()
          .map(conn -> {
            try {
              Trasa trasa = trasaService.getAllTrasy().stream()
                  .filter(t -> t.getNumertrasy().toString().equals(conn.getLineNumber())
                      && t.getNazwatrasy().equals(conn.getLineName()))
                  .findFirst().orElse(null);
              if (trasa == null)
                return conn;

              Kurs kurs = kursService.findAll().stream()
                  .filter(k -> k.getTrasaid() != null && k.getTrasaid().getId().equals(trasa.getId()))
                  .findFirst().orElse(null);
              if (kurs == null)
                return conn;

              List<Przystaneknatrasie> przystanki = trasa.getPrzystankinatrasie().stream()
                  .sorted(Comparator.comparingInt(Przystaneknatrasie::getKolejnosc))
                  .toList();

              // Suma czasów przejazdu do wybranego przystanku początkowego (włącznie z nim)
              int sumaCzasow = 0;
              for (Przystaneknatrasie pnt : przystanki) {
                if (pnt.getCzasprzejazdu() != null) {
                  sumaCzasow += pnt.getCzasprzejazdu();
                }
                if (pnt.getPrzystanekid().getNazwa().equals(conn.getStartStop())) {
                  break;
                }
              }
              java.time.LocalTime godzinaOdjazdu = kurs.getGodzinastartu().plusMinutes(sumaCzasow);
              return new Connection(
                  conn.getLineNumber(),
                  conn.getLineName(),
                  conn.getStartStop(),
                  conn.getEndStop(),
                  godzinaOdjazdu.toString(),
                  conn.getTravelTime());
            } catch (Exception ex) {
              return conn;
            }
          })
          .toList();

      if (found.isEmpty()) {
        Notification.show("Brak połączeń dla podanych przystanków.", 3000, Notification.Position.MIDDLE);
      }
      connectionGrid.setItems(found);
    });

    connectionGrid.addItemClickListener(event -> {
      Connection selected = event.getItem();
      Notification.show("Wybrano połączenie: " + selected.getStartStop() + " -> " + selected.getEndStop());
    });

    VerticalLayout layout = new VerticalLayout(
        new ViewToolbar("Wyszukaj połączenia"),
        startStopField,
        endStopField,
        departureTimeField,
        buttonLayout,
        connectionGrid);
    layout.setSpacing(true);
    layout.setPadding(true);
    add(layout);
  }

  public static class Connection {
    private String lineNumber;
    private String lineName;
    private String startStop;
    private String endStop;
    private String departureTime;
    private String travelTime;

    public Connection(String lineNumber, String lineName, String startStop, String endStop, String departureTime,
        String travelTime) {
      this.lineNumber = lineNumber;
      this.lineName = lineName;
      this.startStop = startStop;
      this.endStop = endStop;
      this.departureTime = departureTime;
      this.travelTime = travelTime;
    }

    public String getLineNumber() {
      return lineNumber;
    }

    public String getLineName() {
      return lineName;
    }

    public String getStartStop() {
      return startStop;
    }

    public String getEndStop() {
      return endStop;
    }

    public String getDepartureTime() {
      return departureTime;
    }

    public String getTravelTime() {
      return travelTime;
    }
  }
}
