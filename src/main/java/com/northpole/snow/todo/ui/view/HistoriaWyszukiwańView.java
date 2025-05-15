package com.northpole.snow.todo.ui.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Historia Wyszukiwań")
@Route("my-view6")
@Menu(order = 5, title = "Historia")
@AnonymousAllowed
public class HistoriaWyszukiwańView extends Composite<VerticalLayout> {

    public HistoriaWyszukiwańView() {
        H1 h1 = new H1();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        h1.setText("Historia ostatnich wyszukiwań");
        h1.setWidth("max-content");
        getContent().add(h1);
    }
}
