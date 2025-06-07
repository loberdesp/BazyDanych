package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.northpole.snow.todo.service.TrasaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.router.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.security.RolesAllowed;

import java.util.ArrayList;
import java.util.List;

@RolesAllowed("ADMIN") // Only admins can access
@Route("admin/dodaj-trase")
@PageTitle("Dodaj trasę")
@Menu(order = 3, icon = "vaadin:plus", title = "Dodaj trasę")
public class AddRouteView extends Main {

  private final TrasaService trasaService;

  @Autowired
  public AddRouteView(TrasaService trasaService) {
    this.trasaService = trasaService;

    TextField routeNumber = new TextField("Numer trasy");
    routeNumber.setPlaceholder("np. 5");
    routeNumber.setWidth("300px");

    TextField routeName = new TextField("Nazwa trasy");
    routeName.setPlaceholder("np. Dworzec - Centrum");
    routeName.setWidth("300px");

    // Dodaj pole na nazwę trasy w drugim kierunku
    TextField routeNameReverse = new TextField("Nazwa trasy w drugą stronę");
    routeNameReverse.setPlaceholder("np. Centrum - Dworzec");
    routeNameReverse.setWidth("300px");

    // Pobierz przystanki z bazy
    List<String> allStops = trasaService.getAllStopsFromDb();

    class StopRow {
      com.vaadin.flow.component.combobox.ComboBox<String> stopName;
      IntegerField travelTime;

      StopRow(boolean first) {
        stopName = new com.vaadin.flow.component.combobox.ComboBox<>("Przystanek");
        stopName.setWidth("200px");
        stopName.setItems(allStops);
        stopName.setAllowCustomValue(false); // tylko przystanki z bazy

        travelTime = new IntegerField("Czas przejazdu (min)");
        travelTime.setWidth("90px");
        if (first) {
          travelTime.setEnabled(false);
          travelTime.setPlaceholder("-");
          travelTime.setValue(0); // ustaw czas 0 dla pierwszego przystanku
        }
      }
    }

    List<StopRow> stopRows = new ArrayList<>();
    VerticalLayout stopsLayout = new VerticalLayout();
    stopsLayout.setSpacing(false);
    stopsLayout.setPadding(false);

    Runnable addStopRow = () -> {
      boolean first = stopRows.isEmpty();
      StopRow row = new StopRow(first);
      stopRows.add(row);
      HorizontalLayout rowLayout = new HorizontalLayout(row.stopName, row.travelTime);
      rowLayout.setSpacing(true);
      stopsLayout.add(rowLayout);
    };

    Button addStopButton = new Button("Dodaj przystanek", e -> addStopRow.run());
    addStopButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // Dodaj pierwszy przystanek domyślnie
    addStopRow.run();

    // Użyj DTO zdefiniowanego w TrasaService

    Button saveButton = new Button("Zapisz trasę", e -> {
      String num = routeNumber.getValue();
      String name = routeName.getValue();
      String nameReverse = routeNameReverse.getValue();
      List<TrasaService.PrzystanekNaTrasieDTO> przystankiNaTrasie = new ArrayList<>();
      boolean valid = true;
      for (int i = 0; i < stopRows.size(); i++) {
        StopRow row = stopRows.get(i);
        String stopVal = row.stopName.getValue();
        if (stopVal == null || stopVal.trim().isEmpty()) {
          valid = false;
          break;
        }
        int czas = (i == 0) ? 0 : (row.travelTime.getValue() != null ? row.travelTime.getValue() : -1);
        if (czas < 0) {
          valid = false;
          break;
        }
        przystankiNaTrasie.add(new TrasaService.PrzystanekNaTrasieDTO(
            num != null ? num.trim() : "",
            stopVal.trim(),
            i + 1,
            czas));
      }
      if (num == null || num.trim().isEmpty() || name == null || name.trim().isEmpty() ||
          nameReverse == null || nameReverse.trim().isEmpty() ||
          przystankiNaTrasie.isEmpty() || !valid) {
        Notification.show("Uzupełnij wszystkie pola i poprawnie podaj czasy przejazdu", 3000,
            Notification.Position.MIDDLE);
        return;
      }

      // Dodaj trasę w pierwszym kierunku
      boolean ok1 = trasaService.addNewRouteWithStopsDTO(num.trim(), name.trim(), przystankiNaTrasie);

      // Przygotuj trasę w drugim kierunku
      List<TrasaService.PrzystanekNaTrasieDTO> reverseStops = new ArrayList<>();
      int n = przystankiNaTrasie.size();
      // Wylicz sumy czasów do każdego przystanku (od początku)
      List<Integer> cumulative = new ArrayList<>();
      int sum = 0;
      for (int i = 0; i < n; i++) {
        sum += (i == 0 ? 0 : przystankiNaTrasie.get(i).czasPrzejazdu);
        cumulative.add(sum);
      }
      // Odwróć kolejność i przelicz czasy
      for (int i = n - 1; i >= 0; i--) {
        String stop = przystankiNaTrasie.get(i).nazwaPrzystanku;
        int idx = n - 1 - i;
        int czas = (idx == 0) ? 0 : cumulative.get(n - 1 - (idx - 1)) - cumulative.get(n - 1 - idx);
        reverseStops.add(new TrasaService.PrzystanekNaTrasieDTO(
            num.trim(),
            stop,
            n - i,
            czas));
      }

      boolean ok2 = trasaService.addNewRouteWithStopsDTO(num.trim(), nameReverse.trim(), reverseStops);

      if (ok1 && ok2) {
        Notification.show("Dodano trasę w obu kierunkach!", 3000, Notification.Position.MIDDLE);
        routeNumber.clear();
        routeName.clear();
        routeNameReverse.clear();
        stopsLayout.removeAll();
        stopRows.clear();
        addStopRow.run();
      } else {
        Notification.show("Błąd podczas dodawania trasy", 3000, Notification.Position.MIDDLE);
      }
    });
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    VerticalLayout leftPanel = new VerticalLayout(
        new ViewToolbar("Dodaj trasę"),
        routeNumber,
        routeName,
        routeNameReverse, // dodaj pole do layoutu
        stopsLayout,
        addStopButton,
        saveButton);
    leftPanel.setSpacing(true);
    leftPanel.setPadding(true);
    leftPanel.setWidth("320px");

    add(leftPanel);
    addClassName(LumoUtility.Padding.LARGE);
  }
}
