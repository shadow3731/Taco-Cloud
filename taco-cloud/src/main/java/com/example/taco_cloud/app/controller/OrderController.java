package com.example.taco_cloud.app.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.taco_cloud.app.common.Urls;
import com.example.taco_cloud.app.model.taco.TacoOrder;
import com.example.taco_cloud.app.service.taco.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private int pageSize = 2;

    @GetMapping("/current")
    public String getOrdersCurrent() {
        return this.getOrdersCurrentView();
    }

    @GetMapping
    public String getOrders(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageIndex = page - 1;
        Page<TacoOrder> pagedOrders = this.orderService.getOrders(pageIndex, pageSize);
        model.addAttribute("orders", pagedOrders.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagedOrders.getTotalPages());
        return Urls.ORDERS.get();
    }

    @PostMapping
    public String postOrders(@Valid TacoOrder order, Errors errors, SessionStatus session) {
        if (errors.hasErrors()) {
            log.error("Order failed: {}", errors.toString());
            return this.getOrdersCurrentView();
        }

        TacoOrder savedOrder = orderService.postOrder(order);
        log.info("Order submitted: {}", savedOrder);
        session.setComplete();

        return "redirect:/" + Urls.HOME.get();
    }

    private String getOrdersCurrentView() {
        String currentUrl = Urls.CURRENT.get();
        currentUrl.replace(currentUrl.charAt(0), Character.toUpperCase(currentUrl.charAt(0)));
        return Urls.ORDERS.get() + currentUrl;
    }
}
