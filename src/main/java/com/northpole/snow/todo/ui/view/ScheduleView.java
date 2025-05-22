package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.router.Menu;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Route("harmonogram")
@PageTitle("Zdefiniuj kurs")
@Menu(order = 7, icon = "vaadin:calendar-clock", title = "Kurs")
public class ScheduleView extends Main {

    public static class ScheduleItem {
        private String lineNumber;
        private String lineName;
        private LocalTime departureTime;

        public ScheduleItem(String lineNumber, String lineName, LocalTime departureTime) {
            this.lineNumber = lineNumber;
            this.lineName = lineName;
            this.departureTime = departureTime;
        }

        public String getLineNumber() {
            return lineNumber;
        }

        public String getLineName() {
            return lineName;
        }

        public LocalTime getDepartureTime() {
            return departureTime;
        }
    }

    private final List<ScheduleItem> scheduleItems = new ArrayList<>();
    private final Grid<ScheduleItem> grid = new Grid<>();

    public ScheduleView() {
        createGrid();

        VerticalLayout layout = new VerticalLayout(
                new ViewToolbar("Zdefiniuj kurs"),
                createForm(),
                grid);

        layout.setSpacing(true);
        layout.setPadding(true);
        add(layout);
        addClassName(LumoUtility.Padding.LARGE);
    }

    private VerticalLayout createForm() {
        ComboBox<String> lineNumber = new ComboBox<>("Numer linii");
        lineNumber.setItems("1", "2", "3", "4", "5");
        lineNumber.setWidth("200px");

        TextField lineName = new TextField("Nazwa linii");
        lineName.setWidth("200px");

        TimePicker departureTime = new TimePicker("Godzina startu");
        departureTime.setWidth("200px");

        Button addButton = new Button("Dodaj kurs", e -> {
            if (lineNumber.isEmpty() || lineName.isEmpty() || departureTime.isEmpty()) {
                Notification.show("Wypełnij wszystkie pola", 3000, Notification.Position.MIDDLE);
                return;
            }
            scheduleItems.add(new ScheduleItem(
                    lineNumber.getValue(),
                    lineName.getValue(),
                    departureTime.getValue()));
            grid.getDataProvider().refreshAll();
            Notification.show("Dodano kurs", 3000, Notification.Position.MIDDLE);
        });
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout fields = new HorizontalLayout(lineNumber, lineName, departureTime, addButton);
        fields.setAlignItems(Alignment.END);

        return new VerticalLayout(fields);
    }

    private void createGrid() {
        grid.addColumn(ScheduleItem::getLineNumber).setHeader("Numer linii").setAutoWidth(true);
        grid.addColumn(ScheduleItem::getLineName).setHeader("Nazwa linii").setAutoWidth(true);
        grid.addColumn(item -> item.getDepartureTime() != null ? item.getDepartureTime().toString() : "")
                .setHeader("Godzina startu").setAutoWidth(true);

        grid.addComponentColumn(item -> {
            Button deleteBtn = new Button("Usuń");
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteBtn.addClickListener(e -> {
                scheduleItems.remove(item);
                grid.getDataProvider().refreshAll();
                Notification.show("Usunięto kurs", 3000, Notification.Position.MIDDLE);
            });
            return deleteBtn;
        }).setHeader("Akcje");

        grid.setHeight("400px");
        grid.setItems(scheduleItems);
    }
}