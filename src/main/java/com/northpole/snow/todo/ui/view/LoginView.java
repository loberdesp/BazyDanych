package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Logowanie")
@Route("my-view")
@Menu(order = 12, title = "Logowanie", icon = "vaadin:sign-in")
@AnonymousAllowed
public class LoginView extends Main {

  public LoginView() {

    EmailField emailField = new EmailField("Email");
    emailField.setWidth("300px");
    PasswordField passwordField = new PasswordField("Hasło");
    passwordField.setWidth("300px");

    Button loginButton = new Button("Zaloguj się", e -> {
      if (emailField.isEmpty() || passwordField.isEmpty()) {
        Notification.show("Wszystkie pola są wymagane.", 3000, Notification.Position.MIDDLE);
        return;
      }
      // Tutaj powinna być logika logowania
      Notification.show("Zalogowano!", 3000, Notification.Position.MIDDLE);
      emailField.clear();
      passwordField.clear();
    });
    loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    VerticalLayout layout = new VerticalLayout(
        new ViewToolbar("Logowanie"),
        emailField,
        passwordField,
        loginButton);
    layout.setSpacing(true);
    layout.setPadding(true);

    add(layout);
  }
}
