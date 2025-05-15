package com.northpole.snow.todo.ui.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

@PageTitle("Logowanie")
@Route("my-view")
@Menu(order = 5, title = "Logowanie")
@AnonymousAllowed
public class LogowanieView extends Composite<VerticalLayout> {

    public LogowanieView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H2 h2 = new H2();
        EmailField emailField = new EmailField();
        PasswordField passwordField = new PasswordField();
        Button buttonPrimary = new Button();
        getContent().addClassName(Gap.SMALL);
        getContent().addClassName(Padding.XSMALL);
        getContent().setWidth("100%");
        getContent().setHeight("1000px");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        h2.setText("Logowanie");
        h2.setWidth("max-content");
        emailField.setLabel("Email");
        emailField.setWidth("300px");
        passwordField.setLabel("Hasło");
        passwordField.setWidth("300px");
        buttonPrimary.setText("Zaloguj się");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(layoutColumn2);
        layoutColumn2.add(h2);
        layoutColumn2.add(emailField);
        layoutColumn2.add(passwordField);
        layoutColumn2.add(buttonPrimary);
    }
}
