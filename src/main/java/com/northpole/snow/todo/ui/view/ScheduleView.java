package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.router.Menu;
import java.time.format.DateTimeFormatter; // Add this import

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route("harmonogram")
@PageTitle("Zdefiniuj harmonogram")
@Menu(order = 7, icon = "vaadin:calendar-clock", title = "Harmonogram")
public class ScheduleView extends Main {

    public static class ScheduleItem {
        private String lineNumber;
        private String direction;
        private LocalTime departureTime;
        private LocalDate validFrom;
        private LocalDate validTo;

        public ScheduleItem(String lineNumber, String direction, LocalTime departureTime,
                LocalDate validFrom, LocalDate validTo) {
            this.lineNumber = lineNumber;
            this.direction = direction;
            this.departureTime = departureTime;
            this.validFrom = validFrom;
            this.validTo = validTo;
        }

        // Getters
        public String getLineNumber() {
            return lineNumber;
        }

        public String getDirection() {
            return direction;
        }

        public LocalTime getDepartureTime() {
            return departureTime;
        }

        public LocalDate getValidFrom() {
            return validFrom;
        }

        public LocalDate getValidTo() {
            return validTo;
        }
    }

    private final List<ScheduleItem> scheduleItems = new ArrayList<>();
    private final Grid<ScheduleItem> grid = new Grid<>();

    public ScheduleView() {
        createForm();
        createGrid();

        VerticalLayout layout = new VerticalLayout(
                new ViewToolbar("Zdefiniuj harmonogram"),
                createForm(),
                grid);

        layout.setSpacing(true);
        layout.setPadding(true);
        add(layout);
        addClassName(LumoUtility.Padding.LARGE);
    }

    private VerticalLayout createForm() {
        // Pola formularza
        ComboBox<String> lineNumber = new ComboBox<>("Numer linii");
        lineNumber.setItems("Linia 1", "Linia 2", "Linia 3", "Linia 4");
        lineNumber.setWidth("300px");

        ComboBox<String> direction = new ComboBox<>("Kierunek");
        direction.setItems("Kierunek A → B", "Kierunek B → A");
        direction.setWidth("300px");

        TimePicker departureTime = new TimePicker("Godzina odjazdu");
        departureTime.setWidth("300px");

        DatePicker validFrom = new DatePicker("Obowiązuje od");
        validFrom.setWidth("300px");

        DatePicker validTo = new DatePicker("Obowiązuje do");
        validTo.setWidth("300px");

        // Przyciski
        Button addButton = new Button("Dodaj do harmonogramu", e -> {
            if (validateForm(lineNumber, direction, departureTime, validFrom, validTo)) {
                addScheduleItem(
                        lineNumber.getValue(),
                        direction.getValue(),
                        departureTime.getValue(),
                        validFrom.getValue(),
                        validTo.getValue());
            }
        });
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.setWidth("300px");

        return new VerticalLayout(
                lineNumber, direction, departureTime,
                validFrom, validTo,
                addButton);
    }

    private void createGrid() {
        grid.addColumn(ScheduleItem::getLineNumber).setHeader("Linia").setAutoWidth(true);
        grid.addColumn(ScheduleItem::getDirection).setHeader("Kierunek").setAutoWidth(true);
        grid.addColumn(item -> item.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .setHeader("Godzina").setAutoWidth(true);
        grid.addColumn(item -> item.getValidFrom().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .setHeader("Obowiązuje od").setAutoWidth(true);
        grid.addColumn(item -> item.getValidTo().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .setHeader("Obowiązuje do").setAutoWidth(true);

        grid.addComponentColumn(item -> {
            Button deleteBtn = new Button(new Icon(VaadinIcon.TRASH));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteBtn.addClickListener(e -> removeScheduleItem(item));
            return deleteBtn;
        }).setHeader("Akcje");

        grid.setHeight("400px");
        grid.setItems(scheduleItems);
    }

    private boolean validateForm(ComboBox<String> lineNumber, ComboBox<String> direction,
            TimePicker departureTime, DatePicker validFrom,
            DatePicker validTo) {
        if (lineNumber.isEmpty() || direction.isEmpty() || departureTime.isEmpty() ||
                validFrom.isEmpty() || validTo.isEmpty()) {
            Notification.show("Wypełnij wszystkie pola", 3000, Notification.Position.MIDDLE);
            return false;
        }

        if (validFrom.getValue().isAfter(validTo.getValue())) {
            Notification.show("Data 'obowiązuje do' musi być późniejsza niż 'obowiązuje od'",
                    3000, Notification.Position.MIDDLE);
            return false;
        }

        return true;
    }

    private void addScheduleItem(String line, String direction, LocalTime time,
            LocalDate from, LocalDate to) {
        ScheduleItem newItem = new ScheduleItem(line, direction, time, from, to);
        scheduleItems.add(newItem);
        grid.getDataProvider().refreshAll();
        Notification.show("Dodano do harmonogramu", 3000, Notification.Position.MIDDLE);
    }

    private void removeScheduleItem(ScheduleItem item) {
        scheduleItems.remove(item);
        grid.getDataProvider().refreshAll();
        Notification.show("Usunięto z harmonogramu", 3000, Notification.Position.MIDDLE);
    }
}