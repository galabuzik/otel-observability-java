package com.paymentology.delivery_service.service;

import com.paymentology.delivery_service.model.Delivery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class DeliveryService {

    public Delivery createDelivery(Delivery delivery) {
        log.info("Creating delivery for source {}, destination {}",  delivery.getSource(), delivery.getDestination());

        delivery = generateDeliveryId(delivery);
        log.info("Order delivery created with id {}, source {}, and destination {} "
                ,  delivery.getId(), delivery.getSource(), delivery.getDestination());
        return delivery;
    }

    private Delivery generateDeliveryId(Delivery delivery) {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').get();
        String randomId = generator.generate(10);
        delivery.setId(randomId);
        return delivery;
    }
}
