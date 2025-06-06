package com.northpole.snow.base.ui.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility.*;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.annotation.security.RolesAllowed;

@Layout
public final class MainLayout extends AppLayout {

    private SideNav sideNav;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);

        sideNav = createSideNav();

        var drawerLayout = new VerticalLayout();
        drawerLayout.setSizeFull();
        drawerLayout.setPadding(false);
        drawerLayout.setSpacing(false);

        drawerLayout.add(sideNav);

        if (isUserLoggedIn()) {
            var logoutButton = createLogoutButton();
            logoutButton.getElement().getStyle().set("margin-top", "auto");
            drawerLayout.add(logoutButton);
        }

        drawerLayout.setFlexGrow(1, sideNav);
        drawerLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        addToDrawer(createHeader(), new Scroller(drawerLayout), createUserMenu());
    }

    private Div createHeader() {
        var appLogo = VaadinIcon.CUBES.create();
        appLogo.addClassNames(TextColor.PRIMARY, IconSize.LARGE);

        var appName = new Span("MPK");
        appName.addClassNames(FontWeight.SEMIBOLD, FontSize.LARGE);

        var header = new Div(appLogo, appName);
        header.addClassNames(Display.FLEX, Padding.MEDIUM, Gap.MEDIUM, AlignItems.CENTER);
        return header;
    }

    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.addClassNames(Margin.Horizontal.MEDIUM);

        MenuConfiguration.getMenuEntries().forEach(entry -> {
            if (isMenuEntryVisible(entry)) {
                nav.addItem(createSideNavItem(entry));
            }
        });

        return nav;
    }

    private Button createLogoutButton() {
        Button logoutButton = new Button("Logout", new Icon(VaadinIcon.SIGN_OUT));
        logoutButton.addClassNames("logout-button");
        logoutButton.addClickListener(e -> UI.getCurrent().getPage().setLocation("/logout"));
        return logoutButton;
    }

    private SideNavItem createSideNavItem(MenuEntry menuEntry) {
        if (menuEntry.icon() != null) {
            return new SideNavItem(menuEntry.title(), menuEntry.path(), new Icon(menuEntry.icon()));
        } else {
            return new SideNavItem(menuEntry.title(), menuEntry.path());
        }
    }

    private Component createUserMenu() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean isLoggedIn = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);

    if (!isLoggedIn) {
        // Jeśli użytkownik anonimowy, nie pokazuj menu użytkownika (zwróć pusty komponent lub null)
        return new Div();  // lub: return null;
    }

    String username = auth.getName();

    var avatar = new Avatar(username);
    avatar.addThemeVariants(AvatarVariant.LUMO_XSMALL);
    avatar.addClassNames(Margin.Right.SMALL);
    avatar.setColorIndex(5);

    var userMenu = new MenuBar();
    userMenu.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
    userMenu.addClassNames(Margin.MEDIUM);

    var userMenuItem = userMenu.addItem(avatar);
    userMenuItem.add(username);
    userMenuItem.getSubMenu().addItem("View Profile");
    userMenuItem.getSubMenu().addItem("Manage Settings");
    userMenuItem.getSubMenu().addItem("Logout", e -> UI.getCurrent().getPage().setLocation("/logout"));

    return userMenu;
}


    private boolean isMenuEntryVisible(MenuEntry entry) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    boolean isAuthenticated = auth != null && auth.isAuthenticated()
            && !(auth instanceof AnonymousAuthenticationToken);

    String path = entry.path();
    if (path == null) {
        return false;
    }

    // Strony tylko dla anonimowych (niezalogowanych)
    if (isAnonymousOnly(path)) {
        return !isAuthenticated;
    }

    // Strony dostępne dla wszystkich (anonimowi + zalogowani)
    if (isPublic(path)) {
        return true;
    }

    // Nie zalogowany nie widzi innych stron
    if (!isAuthenticated) {
        return false;
    }

    // Admin pages: tylko ROLE_ADMIN
    if (path.startsWith("/admin")) {
        return auth.getAuthorities().stream()
                .anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"));
    }

    // User pages: ROLE_USER lub ROLE_ADMIN
    if (path.startsWith("/user")) {
        return auth.getAuthorities().stream()
                .anyMatch(ga -> ga.getAuthority().equals("ROLE_USER") || ga.getAuthority().equals("ROLE_ADMIN"));
    }

    // Inne strony dostępne zalogowanym
    return true;
}

private boolean isAnonymousOnly(String path) {
    return path.equals("/login") || path.equals("/rejestracja");
}

private boolean isPublic(String path) {
    // Tutaj podaj strony dostępne dla wszystkich, np:
    return path.equals("/przegladaj-trase") 
        || path.equals("/wyszukaj-polaczenia") 
        || path.equals("/rozklad-jazdy");
}

    private boolean isAnonymousAllowed(MenuEntry entry) {
        String path = entry.path();
        if (path == null) {
            return false;
        }
        return path.equals("/login") || path.equals("/rejestracja") || path.equals("/przegladaj-trase")
                || path.equals("/wyszukaj-polaczenia") || path.equals("/rozklad-jazdy");
    }

    private boolean isUserLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }
}
