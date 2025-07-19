package com.example.taco_cloud_authorization_server.app.loader;

import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class TestLoader {
    @Bean
    ApplicationRunner runner(List<SecurityFilterChain> chains) {
        return args -> {
            System.out.println("SecurityFilterChains:");
            for (SecurityFilterChain chain : chains) {
                System.out.println(" - " + chain);
            }
        };
    }
}
