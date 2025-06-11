package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.northpole.snow.todo.service.PasazerService;
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
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@RolesAllowed("USER")
@Route("zmiana-hasla")
@PageTitle("Zmiana hasła")
@Menu(order = 14, title = "Zmiana hasła", icon = "vaadin:lock")
public class ChangePasswordView extends Main {

    private final PasazerService pasazerService;

    @Autowired
    public ChangePasswordView(PasazerService pasazerService) {
        this.pasazerService = pasazerService;

        PasswordField oldPasswordField = new PasswordField("Aktualne hasło");
        oldPasswordField.setWidth("300px");

        PasswordField newPasswordField = new PasswordField("Nowe hasło");
        newPasswordField.setWidth("300px");

        PasswordField confirmPasswordField = new PasswordField("Potwierdź nowe hasło");
        confirmPasswordField.setWidth("300px");

        Button changeButton = new Button("Zmień hasło", e -> {
            if (oldPasswordField.isEmpty() || newPasswordField.isEmpty() || confirmPasswordField.isEmpty()) {
                Notification.show("Wszystkie pola są wymagane.", 3000, Notification.Position.MIDDLE);
                return;
            }
            if (!newPasswordField.getValue().equals(confirmPasswordField.getValue())) {
                Notification.show("Nowe hasło i potwierdzenie muszą być takie same.", 3000, Notification.Position.MIDDLE);
                return;
            }

            String login;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                login = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
            } else {
                login = SecurityContextHolder.getContext().getAuthentication().getName();
            }

            // Zmień hasło
            boolean success = pasazerService.changePassword(
                    login,
                    oldPasswordField.getValue(),
                    newPasswordField.getValue()
            );

            if (success) {
                Notification.show("Hasło zostało zmienione!", 3000, Notification.Position.MIDDLE);
                oldPasswordField.clear();
                newPasswordField.clear();
                confirmPasswordField.clear();
            } else {
                Notification.show("Nieprawidłowe aktualne hasło.", 3000, Notification.Position.MIDDLE);
            }
        });
        changeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button clearButton = new Button("Wyczyść", e -> {
            oldPasswordField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();
        });
        clearButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout buttonLayout = new HorizontalLayout(changeButton, clearButton);

        VerticalLayout layout = new VerticalLayout(
                new ViewToolbar("Zmiana hasła"),
                oldPasswordField,
                newPasswordField,
                confirmPasswordField,
                buttonLayout
        );
        layout.setSpacing(true);
        layout.setPadding(true);
        add(layout);
    }
}
