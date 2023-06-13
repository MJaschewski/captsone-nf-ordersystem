package de.neuefische.backend.ordersystem.repository;

import de.neuefische.backend.ordersystem.model.OrderBody;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSystemRepository extends MongoRepository<OrderBody, String> {
}
