package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.northpole.snow.todo.domain.Kurs;
import com.northpole.snow.todo.domain.Przystaneknatrasie;
import com.northpole.snow.todo.domain.Trasa;
import com.northpole.snow.todo.service.ConnectionSearchService;
import com.northpole.snow.todo.service.KursService;
import com.northpole.snow.todo.service.TrasaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    Grid<Connection> connectionGrid = new Grid<>(Connection.class, false);
    connectionGrid.addColumn(Connection::getStartStop).setHeader("Przystanek początkowy");
    connectionGrid.addColumn(Connection::getEndStop).setHeader("Przystanek docelowy");
    connectionGrid.addColumn(Connection::getLineNumber).setHeader("Nr linii");
    connectionGrid.addColumn(Connection::getLineName).setHeader("Nazwa linii");
    connectionGrid.addColumn(Connection::getDepartureTime).setHeader("Czas odjazdu");
    connectionGrid.addColumn(Connection::getTravelTime).setHeader("Czas podróży");
    connectionGrid.setWidthFull();
    connectionGrid.setHeight("300px");

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

    Button resetButton = new Button("Resetuj", e -> {
      startStopField.clear();
      endStopField.clear();
      departureTimeField.clear();
      connectionGrid.setItems(new ArrayList<>());
    });

    HorizontalLayout buttonLayout = new HorizontalLayout(searchButton, resetButton);

    searchButton.addClickListener(e -> {
      String startStop = startStopField.getValue();
      String endStop = endStopField.getValue();
      LocalTime afterTime = departureTimeField.getValue();

      if (startStop == null || endStop == null || startStop.isBlank() || endStop.isBlank()) {
        Notification.show("Podaj oba przystanki!", 3000, Notification.Position.MIDDLE);
        return;
      }

      List<Connection> found = new ArrayList<>();

      for (Trasa trasa : trasaService.getAllTrasy()) {
        List<Przystaneknatrasie> stops = new ArrayList<>(trasa.getPrzystankinatrasie());
        stops.sort(Comparator.comparingInt(Przystaneknatrasie::getKolejnosc));

        int startIdx = -1, endIdx = -1;
        for (int i = 0; i < stops.size(); i++) {
          String name = stops.get(i).getPrzystanekid().getNazwa();
          if (name.equalsIgnoreCase(startStop)) startIdx = i;
          if (name.equalsIgnoreCase(endStop)) endIdx = i;
        }

        if (startIdx == -1 || endIdx == -1 || startIdx >= endIdx) continue;

        int travelMinutes = 0;
        for (int i = startIdx + 1; i <= endIdx; i++) {
          Integer czas = stops.get(i).getCzasprzejazdu();
          if (czas != null) travelMinutes += czas;
        }

        int offsetMinutes = 0;
        for (int i = 0; i <= startIdx; i++) {
          Integer czas = stops.get(i).getCzasprzejazdu();
          if (czas != null) offsetMinutes += czas;
        }

        for (Kurs kurs : trasa.getKurs()) {
          if (kurs.getGodzinastartu() == null) continue;

          LocalTime departureFromStop = kurs.getGodzinastartu().plusMinutes(offsetMinutes);
          if (afterTime != null && departureFromStop.isBefore(afterTime)) continue;

          found.add(new Connection(
              String.valueOf(trasa.getNumertrasy()),
              trasa.getNazwatrasy(),
              startStop,
              endStop,
              departureFromStop.toString(),
              String.format("%02d:%02d", travelMinutes / 60, travelMinutes % 60)
          ));
        }
      }

      if (found.isEmpty()) {
        Notification.show("Brak połączeń dla podanych przystanków.", 3000, Notification.Position.MIDDLE);
      }

      found.sort(Comparator.comparing(conn -> LocalTime.parse(conn.getDepartureTime())));
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
        connectionGrid
    );
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

    public Connection(String lineNumber, String lineName, String startStop, String endStop,
                      String departureTime, String travelTime) {
      this.lineNumber = lineNumber;
      this.lineName = lineName;
      this.startStop = startStop;
      this.endStop = endStop;
      this.departureTime = departureTime;
      this.travelTime = travelTime;
    }

    public String getLineNumber() { return lineNumber; }
    public String getLineName() { return lineName; }
    public String getStartStop() { return startStop; }
    public String getEndStop() { return endStop; }
    public String getDepartureTime() { return departureTime; }
    public String getTravelTime() { return travelTime; }
  }
}

