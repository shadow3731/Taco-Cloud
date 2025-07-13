package com.example.taco_cloud.app.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.taco_cloud.app.model.taco.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {

}
