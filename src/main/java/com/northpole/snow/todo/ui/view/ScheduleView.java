package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.northpole.snow.todo.domain.Kurs;
import com.northpole.snow.todo.domain.Trasa;
import com.northpole.snow.todo.service.KursService;
import com.northpole.snow.todo.service.TrasaService;
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
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RolesAllowed("ADMIN") // Only admins can access
@Route("admin/harmonogram")
@PageTitle("Zdefiniuj kurs")
@Menu(order = 4, icon = "vaadin:calendar-clock", title = "Zarządzaj kursem")
public class ScheduleView extends Main {

    private final KursService kursService;
    private final TrasaService trasaService;
    private final Grid<Kurs> grid = new Grid<>(Kurs.class, false);

    @Autowired
    public ScheduleView(KursService kursService, TrasaService trasaService) {
        this.kursService = kursService;
        this.trasaService = trasaService;

        createGrid();

        VerticalLayout layout = new VerticalLayout(
                new ViewToolbar("Zdefiniuj kurs"),
                createForm(),
                grid);

        layout.setSpacing(true);
        layout.setPadding(true);
        add(layout);
        addClassName(LumoUtility.Padding.LARGE);

        refreshGrid();
    }

    private VerticalLayout createForm() {
        // Pobierz wszystkie trasy
        List<Trasa> trasy = trasaService.getAllTrasy();

        // Zbierz unikalne numery linii
        List<Integer> numeryLinii = trasy.stream()
                .map(Trasa::getNumertrasy)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        // Mapuj numer linii -> lista tras o tym numerze
        Map<Integer, List<Trasa>> trasyByNumer = trasy.stream()
                .collect(Collectors.groupingBy(Trasa::getNumertrasy));

        ComboBox<Integer> lineNumber = new ComboBox<>("Numer linii");
        lineNumber.setItems(numeryLinii);
        lineNumber.setWidth("200px");

        ComboBox<Trasa> lineName = new ComboBox<>("Nazwa linii");
        lineName.setWidth("200px");
        lineName.setItemLabelGenerator(trasa -> trasa.getNazwatrasy());

        lineNumber.addValueChangeListener(e -> {
            Integer selectedNum = e.getValue();
            if (selectedNum != null) {
                List<Trasa> trasyDlaNumeru = trasyByNumer.getOrDefault(selectedNum, List.of());
                lineName.setItems(trasyDlaNumeru);
                lineName.setEnabled(true);
                if (trasyDlaNumeru.size() == 1) {
                    lineName.setValue(trasyDlaNumeru.get(0));
                } else {
                    lineName.clear();
                }
            } else {
                lineName.clear();
                lineName.setItems();
                lineName.setEnabled(false);
            }
        });
        lineName.setEnabled(false);

        TimePicker departureTime = new TimePicker("Godzina startu");
        departureTime.setWidth("200px");
        departureTime.setLocale(new java.util.Locale("pl"));
        departureTime.setStep(java.time.Duration.ofMinutes(30));

        Button addButton = new Button("Dodaj kurs", e -> {
            if (lineNumber.isEmpty() || lineName.isEmpty() || departureTime.isEmpty()) {
                Notification.show("Wypełnij wszystkie pola", 3000, Notification.Position.MIDDLE);
                return;
            }
            // Dodaj kurs po id trasy (nie po numerze)
            boolean ok = kursService.addKursByTrasaId(lineName.getValue().getId(), departureTime.getValue());
            if (ok) {
                Notification.show("Dodano kurs", 3000, Notification.Position.MIDDLE);
                refreshGrid();
            } else {
                Notification.show("Nie udało się dodać kursu", 3000, Notification.Position.MIDDLE);
            }
        });
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout fields = new HorizontalLayout(lineNumber, lineName, departureTime, addButton);
        fields.setAlignItems(Alignment.END);

        return new VerticalLayout(fields);
    }

    private void createGrid() {
        grid.addColumn(k -> k.getTrasaid() != null ? k.getTrasaid().getNumertrasy() : "").setHeader("Numer linii")
                .setAutoWidth(true);
        grid.addColumn(k -> k.getTrasaid() != null ? k.getTrasaid().getNazwatrasy() : "").setHeader("Nazwa linii")
                .setAutoWidth(true);
        grid.addColumn(k -> k.getGodzinastartu() != null ? k.getGodzinastartu().toString() : "")
                .setHeader("Godzina startu").setAutoWidth(true);

        // Dodaj kolumnę z przyciskiem Usuń
        grid.addComponentColumn(kurs -> {
            Button deleteBtn = new Button("Usuń", event -> {
                com.vaadin.flow.component.dialog.Dialog confirmDialog = new com.vaadin.flow.component.dialog.Dialog();
                confirmDialog.add("Czy na pewno chcesz usunąć ten kurs?");
                Button yesBtn = new Button("Tak", e -> {
                    kursService.deleteById(kurs.getId());
                    confirmDialog.close();
                    Notification.show("Usunięto kurs", 3000, Notification.Position.MIDDLE);
                    refreshGrid();
                });
                Button noBtn = new Button("Nie", e -> confirmDialog.close());
                HorizontalLayout dialogBtns = new HorizontalLayout(yesBtn, noBtn);
                confirmDialog.add(dialogBtns);
                confirmDialog.open();
            });
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return deleteBtn;
        }).setHeader("Usuń").setAutoWidth(true);

        grid.setHeight("600px");
    }

    private void refreshGrid() {
        grid.setItems(kursService.findAll());
    }
}