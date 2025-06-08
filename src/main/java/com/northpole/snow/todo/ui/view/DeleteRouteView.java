package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.northpole.snow.todo.domain.Trasa;
import com.northpole.snow.todo.service.TrasaService;
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
import jakarta.annotation.security.RolesAllowed;

import java.util.List;
import java.util.stream.Collectors;

@RolesAllowed("ADMIN")
@Route("admin/usun-trase")
@PageTitle("Usuń trasę")
@Menu(order = 3, icon = "vaadin:trash", title = "Usuń trasę")
public class DeleteRouteView extends Main {

    private final TrasaService trasaService;

    @Autowired
    public DeleteRouteView(TrasaService trasaService) {
        this.trasaService = trasaService;

        // Pole wyboru trasy
        ComboBox<Trasa> routeCombo = new ComboBox<>("Wybierz trasę do usunięcia");
        List<Trasa> allTrasy = trasaService.getAllTrasy();
        routeCombo.setItems(allTrasy);
        routeCombo.setItemLabelGenerator(trasa -> "Linia " + trasa.getNumertrasy() + ": " + trasa.getNazwatrasy());
        routeCombo.setWidth("300px");

        // Przycisk usuń trasę
        Button deleteButton = new Button("Usuń trasę", e -> {
            Trasa selected = routeCombo.getValue();
            if (selected != null) {
                // Sprawdź czy trasa nie jest przypisana do żadnego kursu
                if (selected.getKurs() != null && !selected.getKurs().isEmpty()) {
                    Notification.show("Nie można usunąć trasy, która jest przypisana do kursu!", 4000, Notification.Position.MIDDLE);
                    return;
                }
                trasaService.deleteById(selected.getId());
                Notification.show("Usunięto trasę: Linia " + selected.getNumertrasy() + ": " + selected.getNazwatrasy(), 3000, Notification.Position.MIDDLE);
                routeCombo.setItems(trasaService.getAllTrasy());
                routeCombo.clear();
            } else {
                Notification.show("Wybierz trasę do usunięcia", 3000, Notification.Position.MIDDLE);
            }
        });
        deleteButton.setWidth("300px");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // Layout pól formularza
        VerticalLayout fieldsLayout = new VerticalLayout(routeCombo);
        fieldsLayout.setSpacing(true);
        fieldsLayout.setPadding(false);

        // Layout przycisku
        HorizontalLayout buttonLayout = new HorizontalLayout(deleteButton);

        // Główny layout jak w BusStopView
        VerticalLayout layout = new VerticalLayout(
                new ViewToolbar("Usuń trasę"),
                fieldsLayout,
                buttonLayout);
        layout.setSpacing(true);
        layout.setPadding(true);

        add(layout);
        addClassName(LumoUtility.Padding.LARGE);
    }
}
