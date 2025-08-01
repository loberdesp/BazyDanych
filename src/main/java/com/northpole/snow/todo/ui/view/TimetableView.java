package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import jakarta.annotation.security.RolesAllowed;

import com.northpole.snow.todo.service.PrzystanekService;
import com.northpole.snow.todo.domain.Przystanek;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.dialog.Dialog;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Rozklad jazdy")
@Route("rozklad-jazdy")
@Menu(order = 5, title = "Rozkład jazdy", icon = "vaadin:calendar")
@AnonymousAllowed
@RolesAllowed("USER")
public class TimetableView extends Main {

  private final PrzystanekService przystanekService;

  @Autowired
  public TimetableView(PrzystanekService przystanekService) {
    this.przystanekService = przystanekService;

    TextField stopNameField = new TextField("Nazwa przystanku");
    stopNameField.setWidth("300px");
    TextField streetField = new TextField("Ulica");
    streetField.setWidth("300px");
    TextField lineNumberField = new TextField("Numer linii");
    lineNumberField.setWidth("200px");

    Grid<Przystanek> grid = new Grid<>(Przystanek.class, false);
    grid.addColumn(Przystanek::getNazwa).setHeader("Nazwa");
    grid.addColumn(Przystanek::getUlica).setHeader("Ulica");

    // Domyślnie wyświetl wszystkie przystanki
    grid.setItems(przystanekService.findAll());

    Dialog detailsDialog = new Dialog();
    detailsDialog.setWidth("600px"); // zwiększamy szerokość
    detailsDialog.setHeight("600px"); // zwiększamy wysokość

    grid.asSingleSelect().addValueChangeListener(event -> {
      Przystanek selected = event.getValue();
      detailsDialog.removeAll();
      if (selected != null) {
        var trasyIGodziny = przystanekService.getTrasyIGodziny(selected);

        // Usuwamy powtarzające się trasy po nazwie i numerze trasy
        java.util.List<PrzystanekService.TrasaGodzinaDTO> unikalneTrasy = trasyIGodziny.stream()
            .collect(java.util.stream.Collectors.toMap(
                dto -> dto.nazwaTrasy + "#" + dto.numerTrasy,
                dto -> dto,
                (dto1, dto2) -> dto1))
            .values()
            .stream()
            .sorted(java.util.Comparator.comparing(dto -> {
              try {
                return Integer.parseInt(dto.numerTrasy.toString());
              } catch (Exception e) {
                return Integer.MAX_VALUE;
              }
            }))
            .toList();

        VerticalLayout dialogContent = new VerticalLayout();
        dialogContent.setSizeFull();
        dialogContent.add("Przystanek: " + selected.getNazwa());

        Grid<PrzystanekService.TrasaGodzinaDTO> trasyGrid = new Grid<>(PrzystanekService.TrasaGodzinaDTO.class, false);
        trasyGrid.setAllRowsVisible(true);
        trasyGrid.setWidthFull();
        trasyGrid.setHeight("500px");

        trasyGrid.addColumn(dto -> dto.nazwaTrasy).setHeader("Nazwa trasy").setAutoWidth(true).setFlexGrow(1);
        trasyGrid.addColumn(dto -> dto.numerTrasy).setHeader("Numer trasy").setAutoWidth(true).setFlexGrow(0);

        trasyGrid.setItems(unikalneTrasy);

        // Obsługa kliknięcia w wiersz trasy
        trasyGrid.asSingleSelect().addValueChangeListener(trasaEvent -> {
          PrzystanekService.TrasaGodzinaDTO selectedTrasa = trasaEvent.getValue();
          if (selectedTrasa != null) {
            Dialog godzinyDialog = new Dialog();
            godzinyDialog.setWidth("400px");
            godzinyDialog.setHeight("600px");

            VerticalLayout godzinyLayout = new VerticalLayout();
            godzinyLayout
                .add("Godziny odjazdu dla trasy: " + selectedTrasa.nazwaTrasy + " (" + selectedTrasa.numerTrasy + ")");

            // Pokazuj tylko godziny dla wybranej trasy (nazwa i numer muszą się zgadzać)
            java.util.List<java.time.LocalTime> godziny = trasyIGodziny.stream()
                .filter(dto -> dto.nazwaTrasy.equals(selectedTrasa.nazwaTrasy)
                    && dto.numerTrasy.equals(selectedTrasa.numerTrasy)
                    && dto.godzinaStartu != null)
                .map(dto -> dto.godzinaStartu)
                .sorted()
                .toList();

            Grid<java.time.LocalTime> godzinyGrid = new Grid<>(java.time.LocalTime.class, false);
            godzinyGrid.addColumn(godzina -> godzina.toString()).setHeader("Godzina odjazdu");
            godzinyGrid.setItems(godziny);

            godzinyLayout.add(godzinyGrid);

            Button closeGodzinyButton = new Button("Zamknij", e2 -> godzinyDialog.close());
            closeGodzinyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            godzinyLayout.add(closeGodzinyButton);

            godzinyDialog.add(godzinyLayout);
            godzinyDialog.open();
          }
        });

        dialogContent.add(trasyGrid);

        Button closeButton = new Button("Zamknij", e -> detailsDialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialogContent.add(closeButton);

        detailsDialog.add(dialogContent);
        detailsDialog.open();
      } else {
        detailsDialog.close();
      }
    });

    Button searchButton = new Button("Szukaj", e -> {
      String lineNumber = lineNumberField.getValue();
      String stopName = stopNameField.getValue();
      String street = streetField.getValue();

      if (lineNumber != null && !lineNumber.isBlank()) {
        grid.setItems(przystanekService.searchByAll(stopName, street, lineNumber));
      } else {
        grid.setItems(przystanekService.search(stopName, street));
      }
    });
    searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    Button resetButton = new Button("Resetuj", e -> {
      stopNameField.clear();
      streetField.clear();
      lineNumberField.clear();
      grid.setItems(przystanekService.findAll());
    });

    HorizontalLayout fieldsLayout = new HorizontalLayout(
        stopNameField,
        streetField,
        lineNumberField);

    HorizontalLayout buttonLayout = new HorizontalLayout(searchButton, resetButton);

    VerticalLayout layout = new VerticalLayout(
        new ViewToolbar("Wyszukiwarka przystanków"),
        fieldsLayout,
        buttonLayout,
        grid);
    layout.setSpacing(true);
    layout.setPadding(true);

    add(layout);
    add(detailsDialog); // dodajemy dialog do widoku
  }
}
