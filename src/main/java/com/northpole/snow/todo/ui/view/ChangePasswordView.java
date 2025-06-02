package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("USER")  // Only admins can access
@Route("zmiana-hasla")
@PageTitle("Zmiana hasła")
@Menu(order = 14, title = "Zmiana hasła", icon = "vaadin:lock")
public class ChangePasswordView extends Main {

  public ChangePasswordView() {

    // Password fields
    PasswordField oldPasswordField = new PasswordField("Aktualne hasło");
    oldPasswordField.setWidth("300px");
    PasswordField newPasswordField = new PasswordField("Nowe hasło");
    newPasswordField.setWidth("300px");
    PasswordField confirmPasswordField = new PasswordField("Potwierdź nowe hasło");
    confirmPasswordField.setWidth("300px");

    // Change password button
    Button changeButton = new Button("Zmień hasło", e -> {
      if (oldPasswordField.isEmpty() || newPasswordField.isEmpty() || confirmPasswordField.isEmpty()) {
        Notification.show("Wszystkie pola są wymagane.", 3000, Notification.Position.MIDDLE);
        return;
      }
      if (!newPasswordField.getValue().equals(confirmPasswordField.getValue())) {
        Notification.show("Nowe hasło i potwierdzenie muszą być takie same.", 3000, Notification.Position.MIDDLE);
        return;
      }
      // Here should be the logic for changing the password in the database
      Notification.show("Hasło zostało zmienione!", 3000, Notification.Position.MIDDLE);
      oldPasswordField.clear();
      newPasswordField.clear();
      confirmPasswordField.clear();
    });
    changeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    // Clear fields button
    Button clearButton = new Button("Wyczyść", e -> {
      oldPasswordField.clear();
      newPasswordField.clear();
      confirmPasswordField.clear();
    });
    clearButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    HorizontalLayout buttonLayout = new HorizontalLayout(changeButton, clearButton);

    // Main layout
    VerticalLayout layout = new VerticalLayout(
        new ViewToolbar("Zmiana hasła"),
        oldPasswordField,
        newPasswordField,
        confirmPasswordField,
        buttonLayout);
    layout.setSpacing(true);
    layout.setPadding(true);
    add(layout);
  }
}
