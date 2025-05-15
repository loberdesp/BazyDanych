package com.northpole.snow.todo.ui.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Rejestracja")
@Route("rejestracja")
@Menu(order = 5, title = "Rejestracja")
@AnonymousAllowed
public class RejestracjaView extends Composite<VerticalLayout> {

    public RejestracjaView() {
        H2 h2 = new H2();
        TextField textField = new TextField();
        TextField textField2 = new TextField();
        TextField textField3 = new TextField();
        EmailField emailField = new EmailField();
        PasswordField passwordField = new PasswordField();
        PasswordField passwordField2 = new PasswordField();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        h2.setText("Dane pasażera");
        h2.setWidth("max-content");
        textField.setLabel("Imię");
        getContent().setAlignSelf(FlexComponent.Alignment.START, textField);
        textField.setWidth("300px");
        textField2.setLabel("Nazwisko");
        textField2.setWidth("300px");
        textField3.setLabel("Numer telefonu");
        textField3.setWidth("300px");
        emailField.setLabel("Email");
        emailField.setWidth("300px");
        passwordField.setLabel("Hasło");
        passwordField.setWidth("300px");
        passwordField2.setLabel("Powtórz hasło");
        passwordField2.setWidth("300px");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Zarejestruj się");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSecondary.setText("Anuluj");
        buttonSecondary.setWidth("min-content");
        getContent().add(h2);
        getContent().add(textField);
        getContent().add(textField2);
        getContent().add(textField3);
        getContent().add(emailField);
        getContent().add(passwordField);
        getContent().add(passwordField2);
        getContent().add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
    }
}
