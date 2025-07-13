package com.example.taco_cloud.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taco_cloud.app.model.taco.Taco;

public interface TacoRepository extends JpaRepository<Taco, String> {
    public Optional<Taco> findByName(String name);
}
