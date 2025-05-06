package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.router.Menu;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Route("odblokuj-trase")
@PageTitle("Odblokuj trasę")
@Menu(order = 5, icon = "vaadin:unlock", title = "Odblokuj trasę")
public class UnlockRouteView extends Main {

    public static class RouteBlock {
        private String line;
        private String direction;
        private LocalDateTime blockFrom;
        private LocalDateTime blockTo;
        private String reason;
        private String blockedBy;

        public RouteBlock(String line, String direction, LocalDateTime blockFrom, 
                        LocalDateTime blockTo, String reason, String blockedBy) {
            this.line = line;
            this.direction = direction;
            this.blockFrom = blockFrom;
            this.blockTo = blockTo;
            this.reason = reason;
            this.blockedBy = blockedBy;
        }

        // Getter methods
        public String getLine() { return line; }
        public String getDirection() { return direction; }
        public LocalDateTime getBlockFrom() { return blockFrom; }
        public LocalDateTime getBlockTo() { return blockTo; }
        public String getReason() { return reason; }
        public String getBlockedBy() { return blockedBy; }
    }

    public UnlockRouteView() {
        List<RouteBlock> activeBlocks = getSampleRouteBlocks();

        Grid<RouteBlock> grid = new Grid<>(RouteBlock.class, false);
        
        // Correct column definitions using lambda expressions
        grid.addColumn(route -> route.getLine()).setHeader("Linia").setAutoWidth(true);
        grid.addColumn(route -> route.getDirection()).setHeader("Kierunek").setAutoWidth(true);
        grid.addColumn(route -> 
            route.getBlockFrom().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        ).setHeader("Blokada od").setAutoWidth(true);
        grid.addColumn(route -> 
            route.getBlockTo().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        ).setHeader("Blokada do").setAutoWidth(true);
        grid.addColumn(route -> route.getReason()).setHeader("Powód").setAutoWidth(true);
        grid.addColumn(route -> route.getBlockedBy()).setHeader("Zablokowane przez").setAutoWidth(true);
        
        grid.addComponentColumn(route -> {
            Button unlockBtn = new Button("Odblokuj", new Icon(VaadinIcon.UNLOCK));
            unlockBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
            unlockBtn.addClickListener(e -> unlockRoute(route));
            return unlockBtn;
        }).setHeader("Akcje");

        grid.setItems(activeBlocks);
        grid.setHeight("400px");

        Button refreshBtn = new Button("Odśwież listę", new Icon(VaadinIcon.REFRESH));
        refreshBtn.addClickListener(e -> grid.setItems(getSampleRouteBlocks()));

        VerticalLayout layout = new VerticalLayout();
        layout.add(
            new ViewToolbar("Odblokuj trasę"),
            new HorizontalLayout(refreshBtn),
            grid
        );
        

        layout.setSpacing(true);
        layout.setPadding(true);

        add(layout);
        addClassName(LumoUtility.Padding.LARGE);
    }

    private List<RouteBlock> getSampleRouteBlocks() {
        return Arrays.asList(
            new RouteBlock("Linia 1", "Kierunek A → B", 
                LocalDateTime.now().plusHours(1), 
                LocalDateTime.now().plusDays(1),
                "Remont torowiska", "admin"),
            new RouteBlock("Linia 3", "Kierunek B → A", 
                LocalDateTime.now().minusHours(2), 
                LocalDateTime.now().plusHours(5),
                "Awaria pojazdu", "technik")
        );
    }

    private void unlockRoute(RouteBlock block) {
        Notification.show(
            String.format("Trasa %s (%s) odblokowana przed czasem", block.getLine(), block.getDirection()),
            3000, 
            Notification.Position.MIDDLE
        );
    }
}