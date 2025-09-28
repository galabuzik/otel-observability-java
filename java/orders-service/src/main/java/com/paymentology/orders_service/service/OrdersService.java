package com.paymentology.orders_service.service;

import com.paymentology.orders_service.client.DeliveryClient;
import com.paymentology.orders_service.exception.OrderNotFoundException;
import com.paymentology.orders_service.model.Order;
import com.paymentology.orders_service.repository.OrdersRepository;
import io.micrometer.core.instrument.Metrics;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrdersService {

    private static final String CREATED = "CREATED";
    private static final String IN_DELIVERY = "IN_DELIVERY";

    private OrdersRepository ordersRepository;
    private DeliveryClient deliveryClient;

    public Order createOrder(Order order) {
        log.info("Trying to persist order with details {}", order);
        order.setOrderStatus(CREATED);
        ordersRepository.save(order);
        log.info("Successfully persisted order with details {}", order);
        deliveryClient.createDeliveryForOrder(order);
        order.setOrderStatus(IN_DELIVERY);
        log.info("Successfully created delivery for order with details {}", order);
        return order;
    }

    public Order findOrder(String orderId) {
        log.info("Trying to find order for id {}", orderId);
        return ordersRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Failed to retrieve order with id  {}", orderId);
                    Metrics.counter("counter.orders.received.count.failed")
                            .increment();
                    return new OrderNotFoundException("Order with id " + orderId + " not found");});
    }

}
