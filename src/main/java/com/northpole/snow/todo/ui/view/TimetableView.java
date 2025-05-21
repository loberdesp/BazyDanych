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

@PageTitle("Rozklad jazdy")
@Route("my-view3")
@Menu(order = 6, title = "Rozkład jazdy", icon = "vaadin:calendar")
@AnonymousAllowed
public class TimetableView extends Main {

  public TimetableView() {

    TextField stopNameField = new TextField("Nazwa przystanku");
    stopNameField.setWidth("300px");
    TextField streetField = new TextField("Ulica");
    streetField.setWidth("300px");
    TextField lineNumberField = new TextField("Nr lini");
    lineNumberField.setWidth("300px");

    Button searchButton = new Button("Szukaj", e -> {
      // Tutaj powinna być logika wyszukiwania
      Notification.show("Wyszukiwanie...", 2000, Notification.Position.MIDDLE);
    });
    searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    Button resetButton = new Button("Resetuj", e -> {
      stopNameField.clear();
      streetField.clear();
      lineNumberField.clear();
    });

    HorizontalLayout buttonLayout = new HorizontalLayout(searchButton, resetButton);

    VerticalLayout layout = new VerticalLayout(
        new ViewToolbar("Wyszukiwarka przystanków"),
        stopNameField,
        streetField,
        lineNumberField,
        buttonLayout);
    layout.setSpacing(true);
    layout.setPadding(true);

    add(layout);
  }
}
