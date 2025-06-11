package com.northpole.snow.todo.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle("Logowanie")
@Route(value = "login")
@Menu(order = 12, title = "Logowanie", icon = "vaadin:sign-in")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private TextField usernameField = new TextField("Nazwa użytkownika");
    private PasswordField passwordField = new PasswordField("Hasło");
    private Button loginButton = new Button("Zaloguj się");

    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        FormLayout formLayout = new FormLayout(usernameField, passwordField, loginButton);
        formLayout.setMaxWidth("300px");

        loginButton.addClickListener(e -> {
            if (usernameField.isEmpty() || passwordField.isEmpty()) {
                Notification.show("Wszystkie pola są wymagane.", 3000, Notification.Position.MIDDLE);
                return;
            }

            // Ręczne przesłanie formularza POST do Spring Security
            UI.getCurrent().getPage().executeJs(
                "const form = document.createElement('form');" +
                "form.method = 'POST';" +
                "form.action = '/login';" +
                "form.style.display = 'none';" +
                "const username = document.createElement('input');" +
                "username.name = 'username';" +
                "username.value = $0;" +
                "const password = document.createElement('input');" +
                "password.name = 'password';" +
                "password.value = $1;" +
                "form.appendChild(username);" +
                "form.appendChild(password);" +
                "document.body.appendChild(form);" +
                "form.submit();",
                usernameField.getValue(),
                passwordField.getValue()
            );
        });

        add(formLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            Notification.show("Błędna nazwa użytkownika lub hasło.", 3000, Notification.Position.MIDDLE);
        } else if (event.getLocation().getQueryParameters().getParameters().containsKey("logout")) {
            Notification.show("Zostałeś wylogowany.", 3000, Notification.Position.MIDDLE);
        }
    }
}