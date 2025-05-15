package com.northpole.snow.todo.ui.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PageTitle("Ulubione relacje")
@Route("my-view5")
@Menu(order = 5, title = "Ulubione relacje")
@PermitAll
public class UlubionerelacjeView extends Composite<VerticalLayout> {

    public UlubionerelacjeView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H1 h1 = new H1();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        h1.setText("Lista ulubionych  relacji");
        h1.setWidth("max-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h1);
    }
}
