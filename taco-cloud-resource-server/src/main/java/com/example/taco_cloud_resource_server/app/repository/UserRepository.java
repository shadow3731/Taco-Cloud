package com.example.taco_cloud_resource_server.app.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.taco_cloud_resource_server.app.model.user.User;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
