package com.vaadin.demo.pm.security;

import com.vaadin.demo.pm.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import com.vaadin.snaplets.usermanager.exception.UserDisabledException;
import com.vaadin.snaplets.usermanager.service.PersistentLoginService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

    @Value("${com.vaadin.snaplets.usernamager.encoding.secret.key:1234567890}")
    private String secretKey;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Autowired
    private PersistentLoginService persistentLoginService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                authorize -> authorize.requestMatchers(new AntPathRequestMatcher("/images/*.png")).permitAll());

        http.formLogin(formLogin -> formLogin.failureHandler(authenticationFailureHandler()));
        http.rememberMe(rememberMe -> rememberMe.rememberMeServices(getRememberMeServices())
                .tokenValiditySeconds(7200));
        // Icons from the line-awesome addon
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(new AntPathRequestMatcher("/line-awesome/**/*.svg")).permitAll());
        
        // Access to h2 console
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        AntPathRequestMatcher.antMatcher("/h2-console/**"))
                .permitAll())
                .headers(headers -> headers.frameOptions(config->config.disable()))
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        AntPathRequestMatcher.antMatcher("/h2-console/**")));

        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    public AuthenticationFailureHandler authenticationFailureHandler() {
        Map<String, String> exceptionMappings = new HashMap<>();
        exceptionMappings.put(UserDisabledException.class.getCanonicalName(),
                "/login?error=disabled");
        exceptionMappings.put(BadCredentialsException.class.getCanonicalName(),
                "/login?error=badcredentials");

        ExceptionMappingAuthenticationFailureHandler result =
                new ExceptionMappingAuthenticationFailureHandler();
        result.setExceptionMappings(exceptionMappings);
        result.setDefaultFailureUrl("/login?error");

        return result;
    }

    @Bean
    public PersistentTokenBasedRememberMeServices getRememberMeServices() {
        return new PersistentTokenBasedRememberMeServices(secretKey, userDetailsService,
                persistentLoginService);
    }
}
