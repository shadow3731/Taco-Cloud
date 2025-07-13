package com.example.taco_cloud_authorization_server.app.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.taco_cloud_authorization_server.app.model.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
    boolean existsByClientId(String clientId);
    Optional<Client> findByClientId(String clientId);
}
