package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.northpole.snow.todo.domain.Todo;
import com.northpole.snow.todo.service.TodoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.Clock;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route("tasks")
@PageTitle("Task List")
@Menu(order = 0, icon = "vaadin:clipboard-check", title = "Dodaj Przystanek")
public class TodoView extends Main {

    private final TodoService todoService;

    final TextField description;
    final DatePicker dueDate;
    final Button createBtn;
    final Grid<Todo> todoGrid;

    public TodoView(TodoService todoService, Clock clock) {
        this.todoService = todoService;

        description = new TextField();
        description.setPlaceholder("What do you want to do?");
        description.setAriaLabel("Task description");
        description.setMaxLength(Todo.DESCRIPTION_MAX_LENGTH);
        description.setMinWidth("20em");

        dueDate = new DatePicker();
        dueDate.setPlaceholder("Due date");
        dueDate.setAriaLabel("Due date");

        createBtn = new Button("Create", event -> createTodo());
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withZone(clock.getZone())
                .withLocale(getLocale());
        var dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(getLocale());

        todoGrid = new Grid<>();
        todoGrid.setItems(query -> todoService.list(toSpringPageRequest(query)).stream());
        todoGrid.addColumn(Todo::getDescription).setHeader("Description");
        todoGrid.addColumn(todo -> Optional.ofNullable(todo.getDueDate()).map(dateFormatter::format).orElse("Never"))
                .setHeader("Due Date");
        todoGrid.addColumn(todo -> dateTimeFormatter.format(todo.getCreationDate())).setHeader("Creation Date");
        todoGrid.setSizeFull();

        setSizeFull();
        addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Padding.MEDIUM, LumoUtility.Gap.SMALL);

        add(new ViewToolbar("Dodaj Przystanek"));

        // === DODANE POLA TEKSTOWE I PRZYCISK ===
        // Pole 1: Nazwa przystanku
        TextField stopName = new TextField("Nazwa przystanku");
        stopName.setPlaceholder("Wprowadź nazwę przystanku");
        stopName.setWidth("300px");

        // Pole 2: Adres
        TextField address = new TextField("Lokalizacja");
        address.setPlaceholder("np 56.234.5, 12.3456");
        address.setWidth("300px");

        // Pole 3: Godzina otwarcia
        TextField openingHours = new TextField("Nazwa ulicy");
        openingHours.setPlaceholder("np. Główna 123");
        openingHours.setWidth("300px");

        // Przycisk "Zapisz przystanek"
        Button saveButton = new Button("Zapisz przystanek", e -> {
            Notification.show(
                "Dodano przystanek: " + stopName.getValue() + 
                ", Adres: " + address.getValue() + 
                ", Godziny: " + openingHours.getValue(),
                3000, 
                Notification.Position.MIDDLE
            );
        });
        saveButton.setWidth("300px");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Dodanie komponentów do widoku
        add(stopName, address, openingHours, saveButton);
    }

    private void createTodo() {
        todoService.createTodo(description.getValue(), dueDate.getValue());
        todoGrid.getDataProvider().refreshAll();
        description.clear();
        dueDate.clear();
        Notification.show("Task added", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

}
