package com.example.taco_cloud.app.service.taco;

import com.example.taco_cloud.app.model.taco.Ingredient;

public interface IngredientService {
    Iterable<Ingredient> findAll();
    Ingredient addIngredient(Ingredient ingredient);
}
