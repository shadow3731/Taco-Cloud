package com.example.taco_cloud.app.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.taco_cloud.app.model.user.User;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUsername(String username);
    User findByUsername(String username);
}
