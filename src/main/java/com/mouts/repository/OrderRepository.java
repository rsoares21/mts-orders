package com.mouts.repository;

import com.mouts.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    Optional<Order> findByCodigoOrder(String codigoOrder);
    List<Order> findByStatus(String status);
}
