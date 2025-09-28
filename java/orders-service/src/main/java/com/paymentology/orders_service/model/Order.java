package com.paymentology.orders_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("orders")
public class Order {

    @Id
    private String orderId;
    private String userId;
    private String orderStatus;
    private Double totalPrice;
    private String source;
    private String destination;
}
