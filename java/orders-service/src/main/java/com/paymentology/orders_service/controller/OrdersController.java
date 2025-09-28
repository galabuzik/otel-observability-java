package com.paymentology.orders_service.controller;

import com.paymentology.orders_service.model.Order;
import com.paymentology.orders_service.service.OrdersService;
import io.micrometer.core.instrument.Metrics;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@AllArgsConstructor
@Slf4j
public class OrdersController {

    private OrdersService ordersService;

    @PostMapping("/orders")
    public Order createOrder(@RequestBody Order order) {
        long startTime = System.currentTimeMillis();
        log.info("Creating order with details {}", order);

        Order createdOrder = ordersService.createOrder(order);
        log.info("Successfully created order with details {}", order);
        Metrics.counter("counter.orders.created.count.success").increment();
        long endTime = System.currentTimeMillis();
        Metrics.timer("latencyInSeconds.orders.created")
                .record(endTime -startTime, TimeUnit.MILLISECONDS);
        return createdOrder;
    }

    @GetMapping("/orders/{orderId}")
    public Order getOrder(@PathVariable String orderId) {
        long startTime = System.currentTimeMillis();
        log.info("Searching order with id {}", orderId);
        Order retrievedOrder = ordersService.findOrder(orderId);
        log.info("Successfully found order {} for id {}", retrievedOrder, orderId);
        Metrics.counter("counter.orders.retrieved.count.success").increment();
        long endTime = System.currentTimeMillis();
        Metrics.timer("latencyInSeconds.orders.received")
                .record(endTime -startTime, TimeUnit.MILLISECONDS);
        return retrievedOrder;
    }
}
