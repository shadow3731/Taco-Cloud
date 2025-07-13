package com.example.taco_cloud_resource_server.app.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.taco_cloud_resource_server.app.model.taco.TacoOrder;
import com.example.taco_cloud_resource_server.app.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_readOrders')")
    public Page<TacoOrder> getOrders(@AuthenticationPrincipal Jwt jwt, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        String username = jwt.getSubject();
        return this.orderService.getOrdersByUsername(username, PageRequest.of(page, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_writeOrders')")
    public TacoOrder postOrder(@RequestBody TacoOrder order, @AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        return this.orderService.placeOrder(order, username);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    @PreAuthorize("hasAuthority('SCOPE_writeOrders')")
    public ResponseEntity<TacoOrder> putOrder(@PathVariable String id, @RequestBody TacoOrder order) {
        order.setId(id);
        TacoOrder savedOrder = this.orderService.putOrder(order);
        return ResponseEntity.ok(savedOrder);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    @PreAuthorize("hasAuthority('SCOPE_writeOrders')")
    public ResponseEntity<TacoOrder> patchOrder(@PathVariable String id, @RequestBody TacoOrder order) {
        Optional<TacoOrder> dbOrder = this.orderService.getOrderById(id);
        if (dbOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        TacoOrder patchedOrder = dbOrder.get();
        if (order.getDeliveryName() != null) patchedOrder.setDeliveryName(order.getDeliveryName());
        if (order.getDeliveryCity() != null) patchedOrder.setDeliveryCity(order.getDeliveryCity());
        if (order.getDeliveryState() != null) patchedOrder.setDeliveryState(order.getDeliveryState());
        if (order.getDeliveryStreet() != null) patchedOrder.setDeliveryStreet(order.getDeliveryStreet());
        if (order.getDeliveryZip() != null) patchedOrder.setDeliveryZip(order.getDeliveryZip());
        if (order.getCcNumber() != null) patchedOrder.setCcNumber(order.getCcNumber());
        if (order.getCcExpiration() != null) patchedOrder.setCcExpiration(order.getCcExpiration());
        if (order.getCcCVV() != null) patchedOrder.setCcCVV(order.getCcCVV());

        TacoOrder savedOrder = this.orderService.putOrder(patchedOrder);
        return ResponseEntity.ok(savedOrder);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_deleteOrders')")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable String id) {
        this.orderService.deleteOrderById(id);
        boolean orderExists = this.orderService.existsOrderById(id);

        return !orderExists ?
            ResponseEntity.noContent().build()
            : ResponseEntity.status(HttpStatus.CONFLICT).body(false);
    }
}
