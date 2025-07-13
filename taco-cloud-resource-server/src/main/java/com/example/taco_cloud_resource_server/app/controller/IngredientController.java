package com.example.taco_cloud_resource_server.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.taco_cloud_resource_server.app.model.taco.Ingredient;
import com.example.taco_cloud_resource_server.app.repository.IngredientRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientRepository ingredientRepo;

    @GetMapping
    public Iterable<Ingredient> getIngredients() {
        return this.ingredientRepo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_writeIngredients')")
    public Ingredient postIngredient(@RequestBody Ingredient ingredient) {
        return this.ingredientRepo.save(ingredient);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('SCOPE_deleteIngredients')")
    public void deleteIngredient(@PathVariable String id) {
        this.ingredientRepo.deleteById(id);
    }
}
