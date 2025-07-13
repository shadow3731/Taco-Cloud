package com.example.taco_cloud.app.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.example.taco_cloud.app.model.taco.Ingredient;
import com.example.taco_cloud.app.repository.IngredientRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IngredientByIdConverter implements Converter<String, Ingredient> {
    private final IngredientRepository ingredientRepo;

    @Override
    @Nullable
    public Ingredient convert(@NonNull String id) {
        return this.ingredientRepo.findById(id).orElse(null);
    }    
}
