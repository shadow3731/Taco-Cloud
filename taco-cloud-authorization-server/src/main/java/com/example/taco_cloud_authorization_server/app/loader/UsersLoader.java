package com.example.taco_cloud_authorization_server.app.loader;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.taco_cloud_authorization_server.app.model.User;
import com.example.taco_cloud_authorization_server.app.repository.RoleRepository;
import com.example.taco_cloud_authorization_server.app.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@Order(110)
@RequiredArgsConstructor
public class UsersLoader implements CommandLineRunner {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        List<User> users = this.getInitUsers();
        for (User user : users) {
            if (!this.userRepo.existsByUsername(user.getUsername())) {
                this.userRepo.save(user);
            }
        }
    }

    private List<User> getInitUsers() {
        User rootUser = new User("root", this.encoder.encode("root"), "root", "root", "root", "root", "root", "root");
        rootUser.getRoles().add(this.roleRepo.findById("ROLE_ADMIN").get());

        User admin1User = new User("admin1", this.encoder.encode("admin1"), "admin1", "admin1", "admin1", "admin1", "admin1", "admin1");
        rootUser.getRoles().add(this.roleRepo.findById("ROLE_ADMIN").get());

        User user1User = new User("user1", this.encoder.encode("user1"), "user1", "user1", "user1", "user1", "user1", "user1");
        rootUser.getRoles().add(this.roleRepo.findById("ROLE_USER").get());

        return List.of(rootUser, admin1User, user1User);
    }
}
