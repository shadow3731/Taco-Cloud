package com.example.taco_cloud.app.configuration.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.taco_cloud.app.common.Urls;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {    
    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        registry.addViewController("/" + Urls.HOME.get()).setViewName(Urls.HOME.get());
        registry.addViewController("/").setViewName(Urls.HOME.get());
    }

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(Urls.CLIENT_HOST.get())
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowCredentials(true);
    }
}
