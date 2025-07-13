package com.example.taco_cloud_resource_server.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taco_cloud_resource_server.app.model.taco.Taco;

public interface TacoRepository extends JpaRepository<Taco, String> {
    public Optional<Taco> findByName(String name);
}
