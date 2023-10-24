package com.vaadin.demo.pm.views;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "manager", layout = MainLayout.class)
@AnonymousAllowed
public class ManagerView extends VerticalLayout {

        public ManagerView() {
            add(new H3("Managers View: Only users with the 'MANAGER' role should see this! \uD83D\uDC69\u200D\uD83D\uDCBC"));
        }
}
