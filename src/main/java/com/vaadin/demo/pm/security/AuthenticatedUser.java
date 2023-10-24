package com.vaadin.demo.pm.security;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.snaplets.usermanager.model.UserDto;
import com.vaadin.snaplets.usermanager.service.UserService;

@Component
public class AuthenticatedUser {

    private final UserService userService;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext, UserService userService) {
        this.userService = userService;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public Optional<UserDto> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> userService.findByUsername(userDetails.getUsername())).orElse(Optional.empty());
    }

    public void logout() {
        authenticationContext.logout();
    }

}
