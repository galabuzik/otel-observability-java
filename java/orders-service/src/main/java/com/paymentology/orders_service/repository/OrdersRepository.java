package com.paymentology.orders_service.repository;

import com.paymentology.orders_service.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends CrudRepository<Order, String> {

}
