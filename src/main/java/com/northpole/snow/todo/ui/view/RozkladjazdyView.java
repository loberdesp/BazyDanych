package com.northpole.snow.todo.ui.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Rozklad jazdy")
@Route("my-view3")
@Menu(order = 5, title = "Rozkład jazdy")
@AnonymousAllowed
public class RozkladjazdyView extends Composite<VerticalLayout> {

    public RozkladjazdyView() {
        H2 h2 = new H2();
        HorizontalLayout layoutRow = new HorizontalLayout();
        TextField textField = new TextField();
        TextField textField2 = new TextField();
        TextField textField3 = new TextField();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        Grid<BusStop> busStopGrid = new Grid<>(BusStop.class);
        Grid<Route> routeGrid = new Grid<>(Route.class);
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        h2.setText("Wyszukiwarka przystanków");
        h2.setWidth("max-content");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("100px");
        textField.setLabel("Nazwa przystanku");
        textField.setWidth("300px");
        textField2.setLabel("Ulica");
        textField2.setWidth("300px");
        textField3.setLabel("Nr lini");
        textField3.setWidth("300px");
        buttonPrimary.setText("Szukaj");
        layoutRow.setAlignSelf(FlexComponent.Alignment.CENTER, buttonPrimary);
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSecondary.setText("Resetuj");
        layoutRow.setAlignSelf(FlexComponent.Alignment.CENTER, buttonSecondary);
        buttonSecondary.setWidth("min-content");
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        getContent().add(h2);
        getContent().add(layoutRow);
        layoutRow.add(textField);
        layoutRow.add(textField2);
        layoutRow.add(textField3);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
        getContent().add(layoutColumn2);

        // Konfiguracja tabeli przystanków
        busStopGrid.setColumns("stopName", "street", "lineNumber");
        busStopGrid.getColumnByKey("stopName").setHeader("Nazwa przystanku");
        busStopGrid.getColumnByKey("street").setHeader("Ulica");
        busStopGrid.getColumnByKey("lineNumber").setHeader("Nr linii");
        busStopGrid.setWidthFull();
        busStopGrid.setHeight("300px");
        busStopGrid.addItemClickListener(event -> {
            BusStop selectedStop = event.getItem();
            Notification.show("Wybrano przystanek: " + selectedStop.getStopName());
            // Załaduj dane tras dla wybranego przystanku
            routeGrid.setItems(
                    new Route("Linia 1", "08:00", "08:30"),
                    new Route("Linia 2", "09:00", "09:45"));
        });

        // Konfiguracja tabeli tras
        routeGrid.setColumns("lineName", "departureTime", "arrivalTime");
        routeGrid.getColumnByKey("lineName").setHeader("Linia");
        routeGrid.getColumnByKey("departureTime").setHeader("Czas odjazdu");
        routeGrid.getColumnByKey("arrivalTime").setHeader("Czas przyjazdu");
        routeGrid.setWidthFull();
        routeGrid.setHeight("300px");

        layoutColumn2.add(busStopGrid, routeGrid);
        layoutColumn2.setWidthFull();
        getContent().add(layoutColumn2);

        // Przykładowe dane - należy zastąpić rzeczywistymi danymi z bazy danych
        busStopGrid.setItems(
                new BusStop("Przystanek A", "Ulica A", "1"),
                new BusStop("Przystanek B", "Ulica B", "2"));
    }

    // Klasa reprezentująca przystanek
    public static class BusStop {
        private String stopName;
        private String street;
        private String lineNumber;

        public BusStop(String stopName, String street, String lineNumber) {
            this.stopName = stopName;
            this.street = street;
            this.lineNumber = lineNumber;
        }

        public String getStopName() {
            return stopName;
        }

        public String getStreet() {
            return street;
        }

        public String getLineNumber() {
            return lineNumber;
        }
    }

    // Klasa reprezentująca trasę
    public static class Route {
        private String lineName;
        private String departureTime;
        private String arrivalTime;

        public Route(String lineName, String departureTime, String arrivalTime) {
            this.lineName = lineName;
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
        }

        public String getLineName() {
            return lineName;
        }

        public String getDepartureTime() {
            return departureTime;
        }

        public String getArrivalTime() {
            return arrivalTime;
        }
    }
}
