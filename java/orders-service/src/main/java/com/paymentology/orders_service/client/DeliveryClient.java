package com.paymentology.orders_service.client;

import com.paymentology.orders_service.exception.DeliveryCreationException;
import com.paymentology.orders_service.model.Delivery;
import com.paymentology.orders_service.model.Order;
import io.micrometer.core.instrument.Metrics;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@AllArgsConstructor
public class DeliveryClient {

    private static final String BASE_URL = "http://delivery-service:6001";
    private static final String DELIVERIES_URI = "/deliveries";

    private RestTemplate restTemplate;

    public Delivery createDeliveryForOrder(Order order) {
        long startTime = System.currentTimeMillis();
        log.info("Trying to create delivery for order {}", order);
        Delivery generatedDelivery = fromOrder(order);
        ResponseEntity<Delivery> delivery = null;
        try {
            delivery = restTemplate.postForEntity(BASE_URL + DELIVERIES_URI, generatedDelivery, Delivery.class);
            log.info("Successfully created delivery for order {}", order);
            Metrics.counter("counter.orders.delivery.created.count.success")
                    .increment();
        } catch (Exception e) {
            log.error("Failed to create delivery for order {}", order);
            Metrics.counter("counter.orders.delivery.created.count.failed")
                    .increment();
            throw new DeliveryCreationException("Failed to create delivery for order " + order);
        } finally {
            long endTime = System.currentTimeMillis();
            Metrics.timer("latencyInSeconds.orders.delivery.created")
                    .record(endTime -startTime, TimeUnit.MILLISECONDS);
        }
        return delivery.getBody();
    }

    private Delivery fromOrder(Order order) {
        return Delivery.builder()
                .source(order.getSource())
                .destination(order.getDestination())
                .build();
    }

}
