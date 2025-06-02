package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.northpole.snow.todo.domain.Przystanek;
import com.northpole.snow.todo.domain.PrzystanekRepository;
import com.northpole.snow.todo.service.PrzystanekService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;

@Route("bus-stops")
@PageTitle("Manage Bus Stops")
@Menu(order = 2, icon = "vaadin:map-marker", title = "Usuń przystanek")
public class BusStopView extends Main {

    private final PrzystanekService przystanekService;

    @Autowired
    public BusStopView(PrzystanekService przystanekService) {
        this.przystanekService = przystanekService;

        // Pole wyboru przystanku
        ComboBox<Przystanek> stopCombo = new ComboBox<>("Wybierz przystanek do usunięcia");
        stopCombo.setItems(przystanekService.findAll());
        stopCombo.setItemLabelGenerator(Przystanek::getNazwa);
        stopCombo.setWidth("300px");

        // Przycisk usuń przystanek
        Button deleteButton = new Button("Usuń przystanek", e -> {
            Przystanek selected = stopCombo.getValue();
            if (selected != null) {
                przystanekService.deleteById(selected.getId());
                Notification.show("Usunięto przystanek: " + selected.getNazwa(), 3000, Notification.Position.MIDDLE);
                stopCombo.setItems(przystanekService.findAll());
                stopCombo.clear();
            } else {
                Notification.show("Wybierz przystanek do usunięcia", 3000, Notification.Position.MIDDLE);
            }
        });
        deleteButton.setWidth("300px");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // Layout pól formularza
        VerticalLayout fieldsLayout = new VerticalLayout(stopCombo);
        fieldsLayout.setSpacing(true);
        fieldsLayout.setPadding(false);

        // Layout przycisku
        HorizontalLayout buttonLayout = new HorizontalLayout(deleteButton);

        // Główny layout jak w AddBusStopView
        VerticalLayout layout = new VerticalLayout(
                new ViewToolbar("Usuń przystanek"),
                fieldsLayout,
                buttonLayout);
        layout.setSpacing(true);
        layout.setPadding(true);

        add(layout);
    }
}