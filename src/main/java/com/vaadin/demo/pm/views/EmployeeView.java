package com.vaadin.demo.pm.views;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "employee", layout = MainLayout.class)
@AnonymousAllowed
public class EmployeeView extends VerticalLayout {

        public EmployeeView() {
                add(new H3("Employee View: Only users with the 'EMPLOYEE' role can see this! \uD83E\uDDD1\uD83C\uDFFD\u200D\uD83C\uDFED"));
        }
}
