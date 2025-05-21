package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.router.Menu;

@Route("przegladaj-trase")
@PageTitle("Przeglądaj trasę")
@Menu(order = 3, icon = "vaadin:route", title = "Przeglądaj trasę")
public class BrowseRouteView extends Main {

    public BrowseRouteView() {
        // ComboBox dla wyboru linii
        ComboBox<String> lineNumber = new ComboBox<>("Numer linii");
        lineNumber.setPlaceholder("Wybierz numer linii");
        lineNumber.setItems("Linia 1", "Linia 2", "Linia 3", "Linia 4");
        lineNumber.setWidth("300px");

        // ComboBox dla przystanku początkowego
        ComboBox<String> startStop = new ComboBox<>("Przystanek początkowy");
        startStop.setPlaceholder("Wybierz przystanek początkowy");
        startStop.setWidth("300px");

        // Przycisk wyszukiwania
        Button searchButton = new Button("Wyszukaj trasę", e -> {
            if (lineNumber.getValue() != null && startStop.getValue() != null) {
                Notification.show("Trasa: " + lineNumber.getValue() +

                        Notification.Position.MIDDLE);
            } else {
                Notification.show("Proszę wypełnić wszystkie pola", 3000, Notification.Position.MIDDLE);
            }
        });
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchButton.setWidth("300px");

        // Aktualizacja dostępnych przystanków po wyborze linii
        lineNumber.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                // Tutaj można dodać logikę ładowania przystanków dla wybranej linii
                startStop.setItems("Przystanek A", "Przystanek B", "Przystanek C");
            } else {
                startStop.clear();
                startStop.setItems();
            }
        });

        // Główny layout
        VerticalLayout layout = new VerticalLayout();
        layout.add(
                new ViewToolbar("Przeglądaj trasę"),
                lineNumber,
                startStop,
                searchButton);
        layout.setSpacing(true);
        layout.setPadding(true);

        add(layout);
        addClassName(LumoUtility.Padding.LARGE);
    }
}