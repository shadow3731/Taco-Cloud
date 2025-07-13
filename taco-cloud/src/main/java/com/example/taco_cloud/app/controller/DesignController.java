package com.example.taco_cloud.app.controller;

import java.util.stream.StreamSupport;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.taco_cloud.app.common.Urls;
import com.example.taco_cloud.app.model.taco.Ingredient;
import com.example.taco_cloud.app.model.taco.Taco;
import com.example.taco_cloud.app.model.taco.TacoOrder;
import com.example.taco_cloud.app.model.taco.Ingredient.Type;
import com.example.taco_cloud.app.service.taco.RestIngredientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
@RequiredArgsConstructor
public class DesignController {
    private final RestIngredientService ingredientService;

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        Iterable<Ingredient> ingredients = this.ingredientService.findAll();

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(
                type.toString().toLowerCase(), 
                this.filterByType(ingredients, type)
            );
        }
    }

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String getDesign() {
        return Urls.DESIGN.get();
    }

    @PostMapping
    public String postDesign(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder order) {
        if (errors.hasErrors()) {
            log.error("Failed taco: {}", errors.toString());
            return Urls.DESIGN.get();
        }
        order.addTaco(taco);
        log.info("Processing taco: {}", taco);
        
        return "redirect:/" + Urls.ORDERS.get() + "/" + Urls.CURRENT.get();
    }

    private Iterable<Ingredient> filterByType(Iterable<Ingredient> ingredients, Type type) {
        return StreamSupport.stream(ingredients.spliterator(), false)
            .filter(v -> v.getType().equals(type))
            .toList();
    }
}
