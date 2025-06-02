package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.northpole.snow.todo.service.TrasaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.router.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;
import java.util.ArrayList;

@RolesAllowed("USER")  // Only admins can access
@Route("przegladaj-trase")
@PageTitle("Przeglądaj trasę")
@Menu(order = 6, icon = "vaadin:road", title = "Przeglądaj trasę")
public class BrowseRouteView extends Main {

    private final TrasaService trasaService;

    // Pomocnicza klasa do wyświetlania przystanku i czasu do kolejnego
    private static class StopWithTime {
        private final String stopName;
        private final Integer timeToNext;

        public StopWithTime(String stopName, Integer timeToNext) {
            this.stopName = stopName;
            this.timeToNext = timeToNext;
        }

        public String getStopName() {
            return stopName;
        }

        public Integer getTimeToNext() {
            return timeToNext;
        }
    }

    @Autowired
    public BrowseRouteView(TrasaService trasaService) {
        this.trasaService = trasaService;

        // ComboBox dla wyboru linii
        ComboBox<String> lineNumber = new ComboBox<>("Numer linii");
        lineNumber.setPlaceholder("Wybierz numer linii");
        List<String> allLines = trasaService.getAllLines();
        // Wyświetlaj "Linia X: Nazwa"
        lineNumber.setItems(allLines);
        lineNumber.setItemLabelGenerator(line -> line);
        lineNumber.setWidth("300px");

        // ComboBox dla przystanku początkowego
        ComboBox<String> startStop = new ComboBox<>("Przystanek początkowy");
        startStop.setPlaceholder("Najpierw wybierz linię");
        startStop.setWidth("300px");
        startStop.setEnabled(false);

        // Tabela do wyświetlania przystanków na trasie
        Grid<StopWithTime> stopsGrid = new Grid<>();
        stopsGrid.addColumn(StopWithTime::getStopName).setHeader("Przystanek na trasie");
        stopsGrid.addColumn(s -> s.getTimeToNext() != null ? s.getTimeToNext() + " min" : "-")
                .setHeader("Czas podróży");
        stopsGrid.setWidth("400px");
        stopsGrid.setVisible(false);

        // Przycisk wyszukiwania
        Button searchButton = new Button("Wyszukaj trasę", e -> {
            if (lineNumber.getValue() != null && startStop.getValue() != null) {
                List<String> allStops = trasaService.getStopsForLine(lineNumber.getValue());
                String start = startStop.getValue();
                int startIdx = allStops.indexOf(start);
                if (startIdx >= 0) {
                    // Pobierz czasy kumulujące od wybranego przystanku początkowego
                    List<Integer> times = trasaService.getTimesForLine(lineNumber.getValue(), start);
                    List<StopWithTime> stopsFromStart = new ArrayList<>();
                    for (int i = startIdx, j = 0; i < allStops.size(); i++, j++) {
                        Integer timeToNext = (j < times.size()) ? times.get(j) : null;
                        stopsFromStart.add(new StopWithTime(allStops.get(i), timeToNext));
                    }
                    stopsGrid.setItems(stopsFromStart);
                    stopsGrid.setVisible(true);
                    Notification.show("Trasa: " + lineNumber.getValue() + " od " + startStop.getValue(),
                            3000, Notification.Position.MIDDLE);
                } else {
                    stopsGrid.setItems();
                    stopsGrid.setVisible(false);
                    Notification.show("Nie znaleziono przystanku początkowego na trasie", 3000,
                            Notification.Position.MIDDLE);
                }
            } else {
                stopsGrid.setItems();
                stopsGrid.setVisible(false);
                Notification.show("Proszę wypełnić wszystkie pola", 3000, Notification.Position.MIDDLE);
            }
        });
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchButton.setWidth("300px");

        // Aktualizacja dostępnych przystanków po wyborze linii
        lineNumber.addValueChangeListener(e -> {
            String selectedLine = e.getValue();
            if (selectedLine != null) {
                startStop.setItems(trasaService.getStopsForLine(selectedLine));
                startStop.setEnabled(true);
                startStop.setPlaceholder("Wybierz przystanek początkowy");
            } else {
                startStop.clear();
                startStop.setItems();
                startStop.setEnabled(false);
                startStop.setPlaceholder("Najpierw wybierz linię");
            }
        });

        // Główny layout
        VerticalLayout leftPanel = new VerticalLayout(
                new ViewToolbar("Przeglądaj trasę"),
                lineNumber,
                startStop,
                searchButton);
        leftPanel.setSpacing(true);
        leftPanel.setPadding(true);
        leftPanel.setWidth("320px"); // szerokość panelu z polami

        stopsGrid.setWidth("400px");
        stopsGrid.getStyle().remove("margin-top"); // usuń przesunięcie

        HorizontalLayout mainLayout = new HorizontalLayout(leftPanel, stopsGrid);
        mainLayout.setAlignItems(FlexComponent.Alignment.START);
        mainLayout.setSpacing(true);
        mainLayout.setPadding(false);

        add(mainLayout);
        addClassName(LumoUtility.Padding.LARGE);
    }
}