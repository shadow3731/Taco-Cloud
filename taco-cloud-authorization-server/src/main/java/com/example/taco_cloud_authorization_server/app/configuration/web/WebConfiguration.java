package com.example.taco_cloud_authorization_server.app.configuration.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.taco_cloud_authorization_server.app.common.Urls;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        registry.addViewController("/" + Urls.LOGIN.get()).setViewName(Urls.LOGIN.get());
    }
}
