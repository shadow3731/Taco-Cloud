package com.example.taco_cloud_resource_server.app.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.taco_cloud_resource_server.app.model.taco.TacoOrder;
import com.example.taco_cloud_resource_server.app.model.user.User;
import com.example.taco_cloud_resource_server.app.repository.OrderRepository;
import com.example.taco_cloud_resource_server.app.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepo;
    private final UserRepository userRepo;

    @Transactional
    public boolean existsOrderById(String id) {
        return this.orderRepo.existsById(id);
    }

    @Transactional
    public Optional<TacoOrder> getOrderById(String id) {
        return this.orderRepo.findById(id);
    }

    @Transactional
    public Page<TacoOrder> getOrdersByUsername(String username, Pageable pageable) {
        User user = this.userRepo.findByUsername(username).orElseThrow();
        return this.orderRepo.findByUserOrderByPlacedAtDesc(user, pageable);
    }

    @Transactional
    public TacoOrder placeOrder(TacoOrder order, String username) {
        User user = this.userRepo.findByUsername(username).orElseThrow();
        order.setUser(user);
        order.setPlacedAt(new Date());
        return this.orderRepo.save(order);
    }

    @Transactional
    public TacoOrder putOrder(TacoOrder order) {
        return this.orderRepo.save(order);
    }

    @Transactional
    public void deleteOrderById(String id) {
        this.orderRepo.deleteById(id);
    }
}