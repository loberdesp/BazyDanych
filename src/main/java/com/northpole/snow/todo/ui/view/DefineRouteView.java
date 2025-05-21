package com.northpole.snow.todo.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route("definiuj-trase")
@PageTitle("Zdefiniuj trasę | System Komunikacji Miejskiej")
public class DefineRouteView extends Main {

    public static class BusStop {
        private String id;
        private String name;
        private String address;

        public BusStop(String id, String name, String address) {
            this.id = id;
            this.name = name;
            this.address = address;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        @Override
        public String toString() {
            return name + " (" + address + ")";
        }
    }

    public static class Route {
        private String routeNumber;
        private BusStop startStop;
        private BusStop endStop;
        private List<BusStop> intermediateStops = new ArrayList<>();

        public String getRouteNumber() {
            return routeNumber;
        }

        public void setRouteNumber(String routeNumber) {
            this.routeNumber = routeNumber;
        }

        public BusStop getStartStop() {
            return startStop;
        }

        public void setStartStop(BusStop startStop) {
            this.startStop = startStop;
        }

        public BusStop getEndStop() {
            return endStop;
        }

        public void setEndStop(BusStop endStop) {
            this.endStop = endStop;
        }

        public List<BusStop> getIntermediateStops() {
            return intermediateStops;
        }

        public void setIntermediateStops(List<BusStop> intermediateStops) {
            this.intermediateStops = intermediateStops;
        }
    }

    public DefineRouteView() {
        // Nagłówek
        H2 header = new H2("Zdefiniuj nową trasę");
        header.addClassName("view-header");

        // Binder dla formularza
        Binder<Route> binder = new Binder<>(Route.class);
        Route newRoute = new Route();

        // Formularz
        FormLayout formLayout = new FormLayout();

        // Numer trasy
        TextField routeNumber = new TextField("Numer trasy");
        routeNumber.setPlaceholder("np. 123, A12, M5");
        routeNumber.setRequired(true);
        binder.forField(routeNumber)
                .asRequired("Numer trasy jest wymagany")
                .bind(Route::getRouteNumber, Route::setRouteNumber);

        // Lista przystanków (przykładowe dane)
        List<BusStop> availableStops = List.of(
                new BusStop("1", "Dworzec Główny", "ul. Kolejowa 1"),
                new BusStop("2", "Plac Wolności", "ul. Wolności 5"),
                new BusStop("3", "Rondo Reagana", "al. Solidarności"),
                new BusStop("4", "Osiedle Słoneczne", "ul. Słoneczna 12"),
                new BusStop("5", "Szpital Miejski", "ul. Medyczna 8"),
                new BusStop("6", "Centrum Handlowe", "ul. Handlowa 20"));

        // Przystanek początkowy
        ComboBox<BusStop> startStop = new ComboBox<>("Przystanek początkowy");
        startStop.setItems(availableStops);
        startStop.setItemLabelGenerator(BusStop::toString);
        startStop.setRequired(true);
        binder.forField(startStop)
                .asRequired("Wybierz przystanek początkowy")
                .bind(Route::getStartStop, Route::setStartStop);

        // Przystanek końcowy
        ComboBox<BusStop> endStop = new ComboBox<>("Przystanek końcowy");
        endStop.setItems(availableStops);
        endStop.setItemLabelGenerator(BusStop::toString);
        endStop.setRequired(true);
        binder.forField(endStop)
                .asRequired("Wybierz przystanek końcowy")
                .bind(Route::getEndStop, Route::setEndStop);

        // Przystanki pośrednie
        ComboBox<BusStop> intermediateStop = new ComboBox<>("Dodaj przystanek pośredni");
        intermediateStop.setItems(availableStops);
        intermediateStop.setItemLabelGenerator(BusStop::toString);

        Button addIntermediateStop = new Button("Dodaj", new Icon(VaadinIcon.PLUS));
        addIntermediateStop.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout intermediateLayout = new HorizontalLayout(intermediateStop, addIntermediateStop);

        VerticalLayout intermediateStopsList = new VerticalLayout();
        intermediateStopsList.setPadding(false);
        intermediateStopsList.setSpacing(false);

        addIntermediateStop.addClickListener(e -> {
            BusStop selected = intermediateStop.getValue();
            if (selected != null && !newRoute.getIntermediateStops().contains(selected)) {
                newRoute.getIntermediateStops().add(selected);
                updateIntermediateStopsList(intermediateStopsList, newRoute, binder);
                intermediateStop.clear();
            }
        });

        // Przyciski akcji
        Button saveButton = new Button("Zapisz trasę", new Icon(VaadinIcon.CHECK));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Anuluj", new Icon(VaadinIcon.CLOSE));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonsLayout.setSpacing(true);

        // Układanie formularza
        formLayout.add(routeNumber, startStop, endStop, intermediateLayout, intermediateStopsList);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2));
        formLayout.setColspan(intermediateLayout, 2);
        formLayout.setColspan(intermediateStopsList, 2);

        // Główny układ
        VerticalLayout mainLayout = new VerticalLayout(
                header,
                formLayout,
                buttonsLayout);
        mainLayout.setMaxWidth("800px");
        mainLayout.setPadding(true);
        add(mainLayout);

        // Obsługa zapisywania
        saveButton.addClickListener(e -> {
            try {
                binder.writeBean(newRoute);
                validateRoute(newRoute);
                showSaveConfirmation(newRoute);
            } catch (ValidationException ex) {
                Notification.show("Popraw błędy w formularzu", 3000, Notification.Position.MIDDLE);
            } catch (IllegalArgumentException ex) {
                Notification.show(ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        // Obsługa anulowania
        cancelButton.addClickListener(e -> {
            binder.readBean(new Route());
            newRoute.setIntermediateStops(new ArrayList<>());
            intermediateStopsList.removeAll();
        });
    }

    private void updateIntermediateStopsList(VerticalLayout layout, Route route, Binder<Route> binder) {
        layout.removeAll();

        route.getIntermediateStops().forEach(stop -> {
            HorizontalLayout stopLayout = new HorizontalLayout();
            stopLayout.setSpacing(true);

            TextField stopField = new TextField();
            stopField.setValue(stop.toString());
            stopField.setReadOnly(true);
            stopField.setWidthFull();

            Button removeButton = new Button(new Icon(VaadinIcon.TRASH));
            removeButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            removeButton.addClickListener(e -> {
                route.getIntermediateStops().remove(stop);
                updateIntermediateStopsList(layout, route, binder);
            });

            stopLayout.add(stopField, removeButton);
            stopLayout.expand(stopField);
            layout.add(stopLayout);
        });

        binder.setBean(route);
    }

    private void validateRoute(Route route) {
        if (route.getStartStop().equals(route.getEndStop())) {
            throw new IllegalArgumentException("Przystanek początkowy i końcowy nie mogą być takie same");
        }

        if (route.getIntermediateStops().contains(route.getStartStop())) {
            throw new IllegalArgumentException("Przystanek początkowy nie może być wśród przystanków pośrednich");
        }

        if (route.getIntermediateStops().contains(route.getEndStop())) {
            throw new IllegalArgumentException("Przystanek końcowy nie może być wśród przystanków pośrednich");
        }
    }

    private void showSaveConfirmation(Route route) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Potwierdź utworzenie trasy");

        // Budowanie opisu trasy
        StringBuilder routeDescription = new StringBuilder();
        routeDescription.append("Numer trasy: ").append(route.getRouteNumber()).append("\n");
        routeDescription.append("Przystanek początkowy: ").append(route.getStartStop().getName()).append("\n");

        if (!route.getIntermediateStops().isEmpty()) {
            routeDescription.append("Przystanki pośrednie:\n");
            for (BusStop stop : route.getIntermediateStops()) {
                routeDescription.append(" - ").append(stop.getName()).append("\n");
            }
        }

        routeDescription.append("Przystanek końcowy: ").append(route.getEndStop().getName());

        dialog.setText(routeDescription.toString());

        dialog.setConfirmText("Zapisz");
        dialog.setConfirmButtonTheme("primary success");
        dialog.addConfirmListener(e -> {
            // Tutaj kod zapisujący trasę do bazy danych
            saveRouteToDatabase(route);
            Notification.show("Trasa została pomyślnie zapisana", 3000, Notification.Position.MIDDLE);
        });

        dialog.setCancelable(true);
        dialog.setCancelText("Anuluj");
        dialog.open();
    }

    private void saveRouteToDatabase(Route route) {
        // Implementacja zapisu do bazy danych
        // W rzeczywistej aplikacji tutaj byłoby połączenie z backendem
        System.out.println("Zapisywanie trasy do bazy danych:");
        System.out.println("Numer: " + route.getRouteNumber());
        System.out.println("Start: " + route.getStartStop().getName());
        System.out.println("Koniec: " + route.getEndStop().getName());
        System.out.println("Przystanki pośrednie: " +
                route.getIntermediateStops().stream().map(BusStop::getName).toList());
    }
}