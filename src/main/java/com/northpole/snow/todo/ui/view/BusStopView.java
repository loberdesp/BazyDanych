package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.router.Menu;

@Route("bus-stops")
@PageTitle("Manage Bus Stops")
@Menu(order = 1, icon = "vaadin:map-marker", title = "Usuń przystanek")
public class BusStopView extends Main {

    public BusStopView() {
        // Field for stop name
        TextField stopName = new TextField("Nazwa przystanku");
        stopName.setPlaceholder("Wprowadź nazwę przystanku");
        stopName.setWidth("300px");
        // Save button
        Button saveButton = new Button("Save bus stop", e -> {
        });
        saveButton.setWidth("300px");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Set up the main layout
        setSizeFull();
        addClassNames(
            LumoUtility.BoxSizing.BORDER, 
            LumoUtility.Display.FLEX, 
            LumoUtility.FlexDirection.COLUMN,
            LumoUtility.Padding.MEDIUM, 
            LumoUtility.Gap.SMALL
        );

        // Add toolbar with title
        add(new ViewToolbar("Usuń przystanek"));
        
        // Add form components
        add(stopName, saveButton);
    }
}