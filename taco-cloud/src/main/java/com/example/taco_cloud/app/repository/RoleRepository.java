package com.example.taco_cloud.app.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.taco_cloud.app.model.user.Role;

public interface RoleRepository extends CrudRepository <Role, String> {

}