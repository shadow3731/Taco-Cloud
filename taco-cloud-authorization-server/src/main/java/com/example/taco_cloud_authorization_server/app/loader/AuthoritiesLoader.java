package com.example.taco_cloud_authorization_server.app.loader;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.taco_cloud_authorization_server.app.model.Role;
import com.example.taco_cloud_authorization_server.app.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@Order(100)
@RequiredArgsConstructor
public class AuthoritiesLoader implements CommandLineRunner {
    private final RoleRepository roleRepo;

    @Override
    public void run(String... args) {
        List<String> roles = this.getRoles();
        for (String role : roles) {
            if (!this.roleRepo.existsById(role)) {
                this.roleRepo.save(new Role(role));
            }
        }
    }

    private List<String> getRoles() {
        return List.of("ROLE_USER", "ROLE_ADMIN");
    }
}
