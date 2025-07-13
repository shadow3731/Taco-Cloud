package com.example.taco_cloud.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import com.example.taco_cloud.app.model.taco.TacoOrder;
import com.example.taco_cloud.app.model.user.User;

public interface OrderRepository extends CrudRepository<TacoOrder, String> {
    Page<TacoOrder> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);
    boolean existsById(@NonNull String id);
}