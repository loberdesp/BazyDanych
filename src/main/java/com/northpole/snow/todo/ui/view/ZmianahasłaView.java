package com.northpole.snow.todo.ui.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PageTitle("Zmiana hasła")
@Route("my-view2")
@Menu(order = 5, title = "Zmiana hasła")
@PermitAll
public class ZmianahasłaView extends Composite<VerticalLayout> {

    public ZmianahasłaView() {
        H2 h2 = new H2();
        PasswordField passwordField = new PasswordField();
        PasswordField passwordField2 = new PasswordField();
        PasswordField passwordField3 = new PasswordField();
        Button buttonPrimary = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        h2.setText("Wprowadź dane");
        h2.setWidth("max-content");
        passwordField.setLabel("Aktualne hasło");
        passwordField.setWidth("300px");
        passwordField2.setLabel("Nowe hasło");
        passwordField2.setWidth("300px");
        passwordField3.setLabel("Potwierdź hasło");
        passwordField3.setWidth("300px");
        buttonPrimary.setText("Zmień hasło");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(h2);
        getContent().add(passwordField);
        getContent().add(passwordField2);
        getContent().add(passwordField3);
        getContent().add(buttonPrimary);
    }
}
