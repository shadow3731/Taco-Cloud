package com.example.taco_cloud_resource_server.app.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.taco_cloud_resource_server.app.model.taco.Taco;
import com.example.taco_cloud_resource_server.app.repository.TacoRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("tacos")
@RequiredArgsConstructor
public class TacoController {
    private final TacoRepository tacoRepo;

    @GetMapping(params = "recent")
    @PreAuthorize("hasAuthority('SCOPE_readTacos')")
    public Iterable<Taco> getRecentTacos() {
        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return this.tacoRepo.findAll(page).getContent();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_readTacos')")
    public ResponseEntity<Taco> getTacoById(@PathVariable String id) {
        return tacoRepo.findById(id)
            .map(taco -> ResponseEntity.ok(taco))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasAuthority('SCOPE_readTacos')")
    public ResponseEntity<Taco> getTacoByName(@PathVariable String name) {
        return tacoRepo.findByName(name)
            .map(taco -> ResponseEntity.ok(taco))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = "application/json")
    @PreAuthorize("hasAuthority('SCOPE_writeTacos')")
    @ResponseStatus(HttpStatus.CREATED)
    public Taco postTaco(@RequestBody Taco taco) {
        return tacoRepo.save(taco);
    }
}