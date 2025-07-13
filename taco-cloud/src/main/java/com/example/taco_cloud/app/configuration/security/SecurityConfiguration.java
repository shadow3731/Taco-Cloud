package com.example.taco_cloud.app.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import com.example.taco_cloud.app.common.App;
import com.example.taco_cloud.app.common.Urls;

@Configuration
@EnableGlobalAuthentication
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, OidcUserService oidcUserService, ClientRegistrationRepository clientRepo) throws Exception {
        ClientRegistration client = ((InMemoryClientRegistrationRepository) clientRepo).findByRegistrationId(App.AUTH_CLIENT.get());
        String loginPageUri = "/" + Urls.OAUTH2 + "/" + client.getRegistrationId();
        
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/" + Urls.LOGIN.get(), "/" + Urls.REGISTER.get(), "/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(login -> login
                .loginPage(loginPageUri)
            )
            .oauth2Client(Customizer.withDefaults())
            .logout(Customizer.withDefaults())
            .exceptionHandling(e -> e
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .build();
    }

    @Bean
    OidcUserService oidcUserService() {
        return new OidcUserService();
    }
}
