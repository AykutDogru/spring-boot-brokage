package com.firm.brokage.controller;

import com.firm.brokage.model.Orders;
import com.firm.brokage.services.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    @SecurityRequirement(name = "BasicAuth")
    public ResponseEntity<Orders> createOrder(@RequestBody Orders order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @GetMapping
    @SecurityRequirement(name = "BasicAuth")
    public ResponseEntity<List<Orders>> listOrders(@RequestParam Long customerId, @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(orderService.listOrders(customerId, startDate, endDate));
    }

    @DeleteMapping("/{orderId}")
    @SecurityRequirement(name = "BasicAuth")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/match-orders")
    @SecurityRequirement(name = "BasicAuth")
    public ResponseEntity<Void> matchPendingOrders() {
        orderService.matchPendingOrders();
        return ResponseEntity.noContent().build();
    }
}
