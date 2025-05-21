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

@PageTitle("Wyszukaj połączenia")
@Route("my-view4")
@Menu(order = 9, title = "Wyszukaj połączenia", icon = "vaadin:search")
@AnonymousAllowed
public class SearchConnectionsView extends Main {

  public SearchConnectionsView() {

    TextField startStopField = new TextField("Przystanek początkowy");
    startStopField.setWidth("200px");
    TextField endStopField = new TextField("Przystanek docelowy");
    endStopField.setWidth("200px");
    TimePicker departureTimeField = new TimePicker("Godzina odjazdu");
    departureTimeField.setWidth("200px");

    Button searchButton = new Button("Szukaj");
    searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    Button resetButton = new Button("Resetuj", e -> {
      startStopField.clear();
      endStopField.clear();
      departureTimeField.clear();
    });

    HorizontalLayout buttonLayout = new HorizontalLayout(searchButton, resetButton);

    Grid<Connection> connectionGrid = new Grid<>(Connection.class, false);
    connectionGrid.addColumn(Connection::getStartStop).setHeader("Przystanek początkowy");
    connectionGrid.addColumn(Connection::getEndStop).setHeader("Przystanek docelowy");
    connectionGrid.addColumn(Connection::getDepartureTime).setHeader("Czas odjazdu");
    connectionGrid.setWidthFull();
    connectionGrid.setHeight("300px");

    connectionGrid.setItems(
        new Connection("Przystanek A", "Przystanek B", "08:00", "08:30"),
        new Connection("Przystanek C", "Przystanek D", "09:00", "09:45"));

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
    private String startStop;
    private String endStop;
    private String departureTime;
    private String arrivalTime;

    public Connection(String startStop, String endStop, String departureTime, String arrivalTime) {
      this.startStop = startStop;
      this.endStop = endStop;
      this.departureTime = departureTime;
      this.arrivalTime = arrivalTime;
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

    public String getArrivalTime() {
      return arrivalTime;
    }
  }
}
