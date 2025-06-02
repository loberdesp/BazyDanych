package com.northpole.snow.todo.ui.view;

import com.northpole.snow.todo.domain.Pasazer;
import com.northpole.snow.todo.domain.PasazerRepository;
import com.northpole.snow.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Rejestracja")
@Route("rejestracja")
@Menu(order = 13, title = "Rejestracja", icon = "vaadin:user-check")
@AnonymousAllowed
public class RegistrationView extends Main {

  @Autowired
  private PasazerRepository pasazerRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public RegistrationView() {

    TextField firstNameField = new TextField("Imię");
    firstNameField.setWidth("300px");
    TextField lastNameField = new TextField("Nazwisko");
    lastNameField.setWidth("300px");
    TextField loginField = new TextField("Login");
    lastNameField.setWidth("300px");
    TextField phoneField = new TextField("Numer telefonu");
    phoneField.setWidth("300px");
    EmailField emailField = new EmailField("Email");
    emailField.setWidth("300px");
    PasswordField passwordField = new PasswordField("Hasło");
    passwordField.setWidth("300px");
    PasswordField confirmPasswordField = new PasswordField("Powtórz hasło");
    confirmPasswordField.setWidth("300px");

    Button registerButton = new Button("Zarejestruj się", e -> {
      if (firstNameField.isEmpty() || lastNameField.isEmpty() || phoneField.isEmpty() ||
          emailField.isEmpty() || passwordField.isEmpty() || confirmPasswordField.isEmpty()) {
        Notification.show("Wszystkie pola są wymagane.", 3000, Notification.Position.MIDDLE);
        return;
      }
      if (!passwordField.getValue().equals(confirmPasswordField.getValue())) {
        Notification.show("Hasła muszą być takie same.", 3000, Notification.Position.MIDDLE);
        return;
      }

      // Check if user with this email or login already exists
      if (pasazerRepository.findByEmail(emailField.getValue()).isPresent()) {
        Notification.show("Użytkownik z takim emailem już istnieje.", 3000, Notification.Position.MIDDLE);
        return;
      }
      if (pasazerRepository.findByLogin(loginField.getValue()).isPresent()) {
        Notification.show("Użytkownik z takim loginem już istnieje.", 3000, Notification.Position.MIDDLE);
        return;
      }

      // Create new Pasazer entity and set fields
      Pasazer pasazer = new Pasazer();
      pasazer.setImienazwisko(firstNameField.getValue() + " " + lastNameField.getValue());
      pasazer.setEmail(emailField.getValue());
      pasazer.setNumertelefonu(phoneField.getValue());
      pasazer.setLogin(loginField.getValue());
      pasazer.setHaslo(passwordEncoder.encode(passwordField.getValue()));

      // Save to DB
      pasazerRepository.save(pasazer);

      Notification.show("Zarejestrowano!", 3000, Notification.Position.MIDDLE);

      firstNameField.clear();
      lastNameField.clear();
      phoneField.clear();
      emailField.clear();
      loginField.clear();
      passwordField.clear();
      confirmPasswordField.clear();
    });
    registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    Button cancelButton = new Button("Anuluj", e -> {
      firstNameField.clear();
      lastNameField.clear();
      loginField.clear();
      phoneField.clear();
      emailField.clear();
      passwordField.clear();
      confirmPasswordField.clear();
    });

    HorizontalLayout buttonLayout = new HorizontalLayout(registerButton, cancelButton);

    VerticalLayout layout = new VerticalLayout(
        new ViewToolbar("Dane pasażera"),
        firstNameField,
        lastNameField,
        loginField,
        phoneField,
        emailField,
        passwordField,
        confirmPasswordField,
        buttonLayout);
    layout.setSpacing(true);
    layout.setPadding(true);

    add(layout);
  }
}
