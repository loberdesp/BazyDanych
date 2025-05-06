package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.router.Menu;

import java.time.LocalDateTime;

@Route("blokuj-trase")
@PageTitle("Zablokuj trasę")
@Menu(order = 4, icon = "vaadin:lock", title = "Zablokuj trasę")
public class BlockRouteView extends Main {

    public BlockRouteView() {
        // ComboBox dla wyboru linii
        ComboBox<String> lineNumber = new ComboBox<>("Wybierz linię");
        lineNumber.setItems("Linia 1", "Linia 2", "Linia 3", "Linia 4");
        lineNumber.setWidth("300px");

        // ComboBox dla wyboru kierunku
        ComboBox<String> direction = new ComboBox<>("Kierunek");
        direction.setItems("Kierunek A → B", "Kierunek B → A");
        direction.setWidth("300px");

        // DateTimePicker dla okresu blokady
        DateTimePicker blockFrom = new DateTimePicker("Blokada od");
        blockFrom.setWidth("300px");

        DateTimePicker blockTo = new DateTimePicker("Blokada do");
        blockTo.setWidth("300px");

        // Pole na przyczynę blokady
        TextArea reason = new TextArea("Przyczyna blokady");
        reason.setWidth("300px");
        reason.setMaxLength(200);

        // Przyciski akcji
        Button blockButton = new Button("Zablokuj trasę", e -> blockRoute(
            lineNumber.getValue(),
            direction.getValue(),
            blockFrom.getValue(),
            blockTo.getValue(),
            reason.getValue()
        ));
        blockButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        blockButton.setWidth("300px");

        Button cancelButton = new Button("Anuluj");
        cancelButton.setWidth("300px");

        // Główny layout
        VerticalLayout layout = new VerticalLayout();
        layout.add(
            new ViewToolbar("Zablokuj trasę"),
            lineNumber,
            direction,
            blockFrom,
            blockTo,
            reason,
            blockButton,
            cancelButton
        );
        
        layout.setSpacing(true);
        layout.setPadding(true);

        add(layout);
        addClassName(LumoUtility.Padding.LARGE);
    }

    private void blockRoute(String line, String direction, LocalDateTime from, LocalDateTime to, String reason) {
        if (line == null || direction == null || from == null || to == null) {
            Notification.show("Wypełnij wszystkie wymagane pola", 3000, Notification.Position.MIDDLE);
            return;
        }

        if (from.isAfter(to)) {
            Notification.show("Data końca blokady musi być późniejsza niż początku", 3000, Notification.Position.MIDDLE);
            return;
        }

        // Tutaj dodaj logikę zapisu do bazy danych
        String message = String.format(
            "Trasa %s (%s) zablokowana od %s do %s. Powód: %s",
            line,
            direction,
            reason.isEmpty() ? "brak" : reason
        );

        Notification.show(message, 5000, Notification.Position.MIDDLE);
    }
}