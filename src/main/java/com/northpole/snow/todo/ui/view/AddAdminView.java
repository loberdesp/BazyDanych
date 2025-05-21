package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

@Route("dodaj-admina")
@PageTitle("Dodaj administratora")
public class AddAdminView extends Main implements HasUrlParameter<String> {

    private String userId;

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        this.userId = parameter;
    }

    public AddAdminView() {
        // Formularz
        TextField usernameField = new TextField("Nazwa użytkownika");
        usernameField.setWidth("300px");

        EmailField emailField = new EmailField("Email");
        emailField.setWidth("300px");

        TextField passwordField = new TextField("Tymczasowe hasło");
        passwordField.setWidth("300px");

        // Przyciski
        Button saveButton = new Button("Zapisz administratora", new Icon(VaadinIcon.CHECK));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.setWidth("300px");

        saveButton.addClickListener(e -> {
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Potwierdź nadanie uprawnień");
            dialog.setText(
                    "Czy na pewno chcesz nadać uprawnienia administratora dla " + usernameField.getValue() + "?");

            dialog.setConfirmText("Potwierdź");
            dialog.setConfirmButtonTheme("primary success");
            dialog.addConfirmListener(confirmEvent -> {
                // Tutaj byłaby logika zapisu
                getUI().ifPresent(ui -> ui.navigate("szukaj-uzytkownikow"));
            });

            dialog.setCancelText("Anuluj");
            dialog.open();
        });

        // Układ strony
        VerticalLayout layout = new VerticalLayout(
                new ViewToolbar("Dodaj administratora"),
                new H2(userId != null ? "Awansuj użytkownika" : "Dodaj nowego administratora"),
                usernameField,
                emailField,
                passwordField,
                saveButton);
        layout.setSpacing(true);
        layout.setPadding(true);
        add(layout);
    }
}