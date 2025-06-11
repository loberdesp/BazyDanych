package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.northpole.snow.todo.domain.AdministratorRepository;
import com.northpole.snow.todo.domain.Pasazer;
import com.northpole.snow.todo.domain.PasazerRepository;
import com.northpole.snow.todo.service.PasazerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@RolesAllowed("ADMIN")
@Route("admin/szukaj-uzytkownikow")
@PageTitle("Szukaj użytkowników")
@Menu(order = 15, icon = "vaadin:user", title = "Szukaj użytkowników")
public class UserSearchView extends Main {

    

    public static class User {
        private String id;
        private String username;
        private String email;
        private String role;

        public User(String id, String username, String email, String role) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.role = role;
        }
        // getters i setters
        public String getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    private final PasazerRepository pasazerRepository;
    private final AdministratorRepository administratorRepository;
    private Grid<User> userGrid;
    private TextField searchField;

    @Autowired
    private PasazerService pasazerService;

    @Autowired
    public UserSearchView(PasazerRepository pasazerRepository, AdministratorRepository administratorRepository) {
        this.pasazerRepository = pasazerRepository;
        this.administratorRepository = administratorRepository;

        searchField = new TextField();
        searchField.setPlaceholder("Wpisz nazwę użytkownika lub email...");
        searchField.setWidth("400px");

        Button searchButton = new Button("Szukaj", new Icon(VaadinIcon.SEARCH));
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchButton.addClickListener(e -> refreshGrid());

        userGrid = new Grid<>();
        userGrid.addColumn(User::getUsername).setHeader("Nazwa użytkownika");
        userGrid.addColumn(User::getEmail).setHeader("Email");
        userGrid.addColumn(User::getRole).setHeader("Rola");

        userGrid.addComponentColumn(user -> {
            HorizontalLayout buttons = new HorizontalLayout();

            if ("Administrator".equals(user.getRole())) {
                Button revokeButton = new Button("Odbierz admina");
                revokeButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
                revokeButton.addClickListener(e -> confirmRevokeAdmin(user));
                buttons.add(revokeButton);
            } else {
                Button promoteButton = new Button("Awansuj", new Icon(VaadinIcon.USER_CHECK));
                promoteButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
                promoteButton.addClickListener(e -> {
                    pasazerService.promoteToAdmin(Integer.valueOf(user.getId()));
                    refreshGrid();
                });
                buttons.add(promoteButton);
            }

            return buttons;
        }).setHeader("Akcje");

        VerticalLayout layout = new VerticalLayout(
                new ViewToolbar("Zarządzanie użytkownikami"),
                new HorizontalLayout(searchField, searchButton),
                userGrid);
        layout.setSpacing(true);
        layout.setPadding(true);
        add(layout);

        refreshGrid();
    }

    private void refreshGrid() {
        String filter = searchField.getValue() == null ? "" : searchField.getValue().toLowerCase();

        List<Pasazer> pasazerowie = pasazerRepository.findAll();

        List<User> users = pasazerowie.stream()
                .filter(p -> p.getLogin().toLowerCase().contains(filter) || p.getEmail().toLowerCase().contains(filter))
                .map(p -> {
                    boolean isAdmin = administratorRepository.existsByPasazerid(p);
                    String role = isAdmin ? "Administrator" : "Użytkownik";
                    return new User(p.getId().toString(), p.getLogin(), p.getEmail(), role);
                })
                .collect(Collectors.toList());

        userGrid.setItems(users);
    }

    private void confirmRevokeAdmin(User user) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Potwierdź odebranie uprawnień");
        dialog.setText("Czy na pewno chcesz odebrać uprawnienia administratora użytkownikowi " + user.getUsername() + "?");

        dialog.setConfirmText("Odbierz uprawnienia");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(e -> {
            pasazerRepository.findById(Integer.valueOf(user.getId())).ifPresent(pasazer -> {
                administratorRepository.findByPasazerid(pasazer).ifPresent(admin -> {
                    administratorRepository.delete(admin);
                    refreshGrid();
                });
            });
        });

        dialog.setCancelable(true);
        dialog.setCancelText("Anuluj");
        dialog.open();
    }
}
