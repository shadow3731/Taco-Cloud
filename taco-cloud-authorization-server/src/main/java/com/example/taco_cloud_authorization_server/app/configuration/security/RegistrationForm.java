package com.example.taco_cloud_authorization_server.app.configuration.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.taco_cloud_authorization_server.app.model.User;

import lombok.Data;

@Data
public class RegistrationForm {
    private String username;
    private String password;
    private String fullname;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;

    public User toUser(PasswordEncoder encoder) {
        return new User(username, encoder.encode(password), fullname, street, city, state, zip, phoneNumber);
    }
}
