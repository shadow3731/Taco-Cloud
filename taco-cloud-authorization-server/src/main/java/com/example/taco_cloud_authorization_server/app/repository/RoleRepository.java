package com.example.taco_cloud_authorization_server.app.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.taco_cloud_authorization_server.app.model.Role;

public interface RoleRepository extends CrudRepository<Role, String> {

}
