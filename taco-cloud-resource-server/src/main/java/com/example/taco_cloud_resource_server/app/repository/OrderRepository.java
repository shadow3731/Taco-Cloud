package com.example.taco_cloud_resource_server.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import com.example.taco_cloud_resource_server.app.model.taco.TacoOrder;
import com.example.taco_cloud_resource_server.app.model.user.User;

public interface OrderRepository extends CrudRepository<TacoOrder, String> {
    Page<TacoOrder> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);
    boolean existsById(@NonNull String id);
}