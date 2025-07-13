package com.example.taco_cloud_authorization_server.app.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.taco_cloud_authorization_server.app.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
