package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.northpole.snow.todo.domain.Przystanek;
import com.northpole.snow.todo.domain.PrzystanekRepository;
import com.northpole.snow.todo.service.PrzystanekService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;

@Route("add-bus-stop")
@PageTitle("Dodaj przystanek")
@Menu(order = 1, icon = "vaadin:plus", title = "Dodaj przystanek")
public class AddBusStopView extends Main {


private final PrzystanekService przystanekService;

  @Autowired
  public AddBusStopView(PrzystanekService przystanekService) {
    this.przystanekService = przystanekService;
    

    // Pole: nazwa przystanku
    TextField stopName = new TextField("Nazwa przystanku");
    stopName.setPlaceholder("Wprowadź nazwę przystanku");
    stopName.setWidth("300px");

    // Pole: ulica
    TextField street = new TextField("Ulica");
    street.setPlaceholder("Wprowadź ulicę");
    street.setWidth("300px");

    // Pole: położenie (opcjonalnie)
    TextArea location = new TextArea("Położenie (opcjonalnie)");
    location.setPlaceholder("Wprowadź położenie lub zostaw puste");
    location.setWidth("300px");

    // Przycisk zapisz przystanek
    Button saveButton = new Button("Zapisz przystanek", e -> {
      String nazwa = stopName.getValue();
      String ulica = street.getValue();
      String polozenie = location.getValue();

      if (nazwa == null || nazwa.isBlank() || ulica == null || ulica.isBlank()) {
        Notification.show("Nazwa i ulica są wymagane!", 3000, Notification.Position.MIDDLE);
        return;
      }

      Przystanek przystanek = new Przystanek();
      przystanek.setNazwa(nazwa);
      przystanek.setUlica(ulica);
      przystanek.setPolozenie(polozenie);

      przystanekService.save(przystanek);

      Notification.show("Dodano przystanek: " + nazwa, 3000, Notification.Position.MIDDLE);
      stopName.clear();
      street.clear();
      location.clear();
    });
    saveButton.setWidth("300px");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    // Layout pól formularza
    VerticalLayout fieldsLayout = new VerticalLayout(
        stopName,
        street,
        location);
    fieldsLayout.setSpacing(true);
    fieldsLayout.setPadding(false);

    // Layout przycisku
    HorizontalLayout buttonLayout = new HorizontalLayout(saveButton);

    // Główny layout jak w tabletime (TimetableView)
    VerticalLayout layout = new VerticalLayout(
        new ViewToolbar("Dodaj przystanek"),
        fieldsLayout,
        buttonLayout);
    layout.setSpacing(true);
    layout.setPadding(true);

    add(layout);
  }
}
