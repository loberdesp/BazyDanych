package com.northpole.snow.todo.ui.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Wyszukaj połączenia")
@Route("my-view4")
@Menu(order = 5, title = "Wyszukaj połączenia")
@AnonymousAllowed
public class WyszukajpołączeniaView extends Composite<VerticalLayout> {

    public WyszukajpołączeniaView() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H2 h2 = new H2();
        TextField textField = new TextField();
        TextField textField2 = new TextField();
        TimePicker timePicker = new TimePicker();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();
        VerticalLayout layoutColumn3 = new VerticalLayout();
        Grid<Connection> connectionGrid = new Grid<>(Connection.class);

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        layoutColumn2.getStyle().set("flex-grow", "1");
        h2.setText("Dane trasy");
        h2.setWidth("max-content");
        textField.setLabel("Przystanek początkowy");
        textField.setWidth("200px");
        textField2.setLabel("Przystanek docelowy");
        textField2.setWidth("200px");
        timePicker.setLabel("Godzina odjazdu");
        timePicker.setWidth("200px");
        layoutRow2.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Szukaj");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSecondary.setText("Resetuj");
        buttonSecondary.setWidth("min-content");
        layoutColumn3.setWidth("100%");
        layoutColumn3.getStyle().set("flex-grow", "1");
        getContent().add(layoutRow);
        layoutRow.add(layoutColumn2);
        layoutColumn2.add(h2);
        layoutColumn2.add(textField);
        layoutColumn2.add(textField2);
        layoutColumn2.add(timePicker);
        layoutColumn2.add(layoutRow2);
        layoutRow2.add(buttonPrimary);
        layoutRow2.add(buttonSecondary);
        layoutRow.add(layoutColumn3);

        // Konfiguracja tabeli połączeń
        connectionGrid.setColumns("startStop", "endStop", "departureTime");
        connectionGrid.getColumnByKey("startStop").setHeader("Przystanek początkowy");
        connectionGrid.getColumnByKey("endStop").setHeader("Przystanek docelowy");
        connectionGrid.getColumnByKey("departureTime").setHeader("Czas odjazdu");
        connectionGrid.setWidthFull();
        connectionGrid.setHeight("300px");

        connectionGrid.addItemClickListener(event -> {
            Connection selectedConnection = event.getItem();
            Notification.show("Wybrano połączenie: " + selectedConnection.getStartStop() + " -> "
                    + selectedConnection.getEndStop());
            // Logika przejścia do panelu kursu
        });

        layoutColumn3.add(connectionGrid);
        layoutColumn3.setWidthFull();
        layoutColumn3.getStyle().set("flex-grow", "1");
        getContent().add(layoutColumn3);

        // Przykładowe dane - należy zastąpić rzeczywistymi danymi z bazy danych
        connectionGrid.setItems(
                new Connection("Przystanek A", "Przystanek B", "08:00", "08:30"),
                new Connection("Przystanek C", "Przystanek D", "09:00", "09:45"));
    }

    // Klasa reprezentująca połączenie
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
