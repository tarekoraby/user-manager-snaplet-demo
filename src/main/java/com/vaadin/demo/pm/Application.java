package com.vaadin.demo.pm;

import com.vaadin.demo.pm.views.MainLayout;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import com.vaadin.flow.theme.Theme;
import com.vaadin.snaplets.SnapletsAutoConfiguration;
import com.vaadin.snaplets.usermanager.UserManagerAutoConfiguration;
import com.vaadin.snaplets.usermanager.flow.util.RouteConfigurer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@NpmPackage(value = "@fontsource/cabin", version = "4.5.0")
@Theme(value = "projectsmanager")
@Push
@EnableVaadin({"com.vaadin.snaplets.usermanager.vaadin", "com.vaadin.demo.pm"})
@EntityScan(basePackageClasses = Application.class)
@ComponentScan(basePackageClasses = {UserManagerAutoConfiguration.class, SnapletsAutoConfiguration.class, Application.class})
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    RouteConfigurer routeConfigurer;

    @PostConstruct
    public void configure() {
      routeConfigurer.setViewsRouterLayout(MainLayout.class);
    }
}
