package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PageTitle("Ulubione relacje")
@Route("my-view5")
@Menu(order = 10, title = "Ulubione relacje", icon = "vaadin:star")
@PermitAll
public class FavoriteRelationsView extends Main {

  public FavoriteRelationsView() {
    H1 header = new H1("Lista ulubionych relacji");
    header.setWidth("max-content");

    VerticalLayout layout = new VerticalLayout(
        new ViewToolbar("Ulubione relacje"),
        header);
    layout.setSpacing(true);
    layout.setPadding(true);

    add(layout);
  }
}
