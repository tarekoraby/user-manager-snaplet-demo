package com.vaadin.demo.pm.views;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "public", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class PublicView extends VerticalLayout {

        public PublicView() {
            add(new H3("Public view: Anybody can see this! \uD83D\uDE0A"));
        }
}
