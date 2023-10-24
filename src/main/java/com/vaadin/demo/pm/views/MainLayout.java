package com.vaadin.demo.pm.views;

import com.vaadin.demo.pm.security.AuthenticatedUser;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.snaplets.usermanager.flow.view.AuthoritiesView;
import com.vaadin.snaplets.usermanager.flow.view.ChangePasswordView;
import com.vaadin.snaplets.usermanager.flow.view.GroupsListView;
import com.vaadin.snaplets.usermanager.flow.view.ProfileView;
import com.vaadin.snaplets.usermanager.flow.view.RulesView;
import com.vaadin.snaplets.usermanager.flow.view.UsersListView;
import com.vaadin.snaplets.usermanager.flow.view.ViewsView;
import com.vaadin.snaplets.usermanager.model.UserDto;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private final AuthenticatedUser authenticatedUser;
    private final AccessAnnotationChecker accessChecker;
    private H2 viewTitle;

    private SideNav nav;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("My App");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        nav = new SideNav();
        Scroller scroller = new Scroller(nav);

        addNavItems();

        addToDrawer(header, scroller, createFooter());
    }

    private void addNavItems() {
        addNavItem("Public", PublicView.class, LineAwesomeIcon.GLOBE_SOLID.create());
        addNavItem("Employee", EmployeeView.class, LineAwesomeIcon.USER_SOLID.create());
        addNavItem("Manager", ManagerView.class, LineAwesomeIcon.USER_TIE_SOLID.create());


        addNavItem("Users", UsersListView.class, LineAwesomeIcon.USER.create());
        addNavItem("Roles", AuthoritiesView.class, LineAwesomeIcon.THEATER_MASKS_SOLID.create());
        addNavItem("Groups", GroupsListView.class, LineAwesomeIcon.USERS_SOLID.create());
        addNavItem("Rules", RulesView.class, LineAwesomeIcon.BALANCE_SCALE_SOLID.create());
        addNavItem("Views", ViewsView.class, LineAwesomeIcon.EYE_SOLID.create());
        addNavItem("Change Password", ChangePasswordView.class, LineAwesomeIcon.KEY_SOLID.create());
    }

    private void addNavItem(String label, Class<? extends Component> view, Component prefix) {
        if (accessChecker.hasAccess(view)) {
            nav.addItem(new SideNavItem(label, view, prefix));
        }
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<UserDto> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            UserDto user = maybeUser.get();

            Avatar avatar = new Avatar(user.getUsername());
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getUsername());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("My profile", e -> {
                UI.getCurrent().navigate(ProfileView.class);
            });
            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
