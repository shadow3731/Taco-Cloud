package com.example.taco_cloud_resource_server.app.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.taco_cloud_resource_server.app.model.taco.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {

}
