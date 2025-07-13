package com.example.taco_cloud.app.model.taco;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.rest.core.annotation.RestResource;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@RestResource(rel = "tacos", path = "tacos")
public class Taco {
    @Id
    private String id;

    private Date createdAt = new Date();

    @NotNull
    @Size(min = 5, message = "Minimum 5 characters!")
    private String name;

    @Size(min = 1, message = "Must have at least 1 ingredient!")
    @ManyToMany()
    @JoinTable(
        name = "ingredient_ref",
        joinColumns = @JoinColumn(name = "taco_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient")
    )
    private List<Ingredient> ingredients = new ArrayList<>();

    @PrePersist
    public void generateIdIfNull() {
        if (this.id == null) id = UUID.randomUUID().toString();
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }
}
