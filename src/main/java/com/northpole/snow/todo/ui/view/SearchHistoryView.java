package com.northpole.snow.todo.ui.view;

import com.northpole.snow.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("USER")  // Only admins can access
@PageTitle("Historia Wyszukiwań")
@Route("my-view6")
@Menu(order = 11, title = "Historia", icon = "vaadin:book")
public class SearchHistoryView extends Main {

  public SearchHistoryView() {

    VerticalLayout layout = new VerticalLayout(
        new ViewToolbar("Historia ostatnich wyszukiwań")

    );
    layout.setSpacing(true);
    layout.setPadding(true);

    add(layout);
  }
}
