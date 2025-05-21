package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
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

@Route("szukaj-uzytkownikow")
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

        public String getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    public UserSearchView() {
        // Pole wyszukiwania
        TextField searchField = new TextField();
        searchField.setPlaceholder("Wpisz nazwę użytkownika lub email...");
        searchField.setWidth("400px");

        Button searchButton = new Button("Szukaj", new Icon(VaadinIcon.SEARCH));
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Tabela wyników
        Grid<User> userGrid = new Grid<>();
        userGrid.addColumn(User::getUsername).setHeader("Nazwa użytkownika");
        userGrid.addColumn(User::getEmail).setHeader("Email");
        userGrid.addColumn(User::getRole).setHeader("Rola");

        userGrid.addComponentColumn(user -> {
            HorizontalLayout buttons = new HorizontalLayout();

            if ("Administrator".equals(user.getRole())) {
                Button revokeButton = new Button("Odbierz admina");
                revokeButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
                revokeButton.addClickListener(e -> confirmRevokeAdmin(user, userGrid));
                buttons.add(revokeButton);
            } else {
                Button promoteButton = new Button("Awansuj", new Icon(VaadinIcon.USER_CHECK));
                promoteButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
                promoteButton.addClickListener(e -> {
                    getUI().ifPresent(ui -> ui.navigate("dodaj-admina/" + user.getId()));
                });
                buttons.add(promoteButton);
            }

            return buttons;
        }).setHeader("Akcje");

        // Przykładowe dane
        userGrid.setItems(
                new User("1", "jan_kowalski", "jan@example.com", "Użytkownik"),
                new User("2", "anna_nowak", "anna@example.com", "Moderator"),
                new User("3", "admin", "admin@example.com", "Administrator"));

        // Link do dodawania nowego admina
        RouterLink addAdminLink = new RouterLink("Dodaj nowego administratora", AddAdminView.class);
        addAdminLink.addClassName(LumoUtility.Margin.Top.LARGE);

        // Układ strony
        VerticalLayout layout = new VerticalLayout(
                new ViewToolbar("Zarządzanie użytkownikami"),
                new HorizontalLayout(searchField, searchButton),
                userGrid,
                addAdminLink);
        layout.setSpacing(true);
        layout.setPadding(true);
        add(layout);
    }

    private void confirmRevokeAdmin(User user, Grid<User> grid) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Potwierdź odebranie uprawnień");
        dialog.setText(
                "Czy na pewno chcesz odebrać uprawnienia administratora użytkownikowi " + user.getUsername() + "?");

        dialog.setConfirmText("Odbierz uprawnienia");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(e -> {
            user.setRole("Użytkownik");
            grid.getDataProvider().refreshItem(user);

        });

        dialog.setCancelable(true);
        dialog.setCancelText("Anuluj");
        dialog.open();
    }
}