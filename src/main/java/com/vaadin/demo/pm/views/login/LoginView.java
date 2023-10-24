package com.vaadin.demo.pm.views.login;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.snaplets.usermanager.flow.view.UserManagerLoginView;

@AnonymousAllowed
@Route(value = "login")
public class LoginView extends UserManagerLoginView {


}