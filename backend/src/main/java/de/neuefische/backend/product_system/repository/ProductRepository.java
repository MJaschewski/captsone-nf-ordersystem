package de.neuefische.backend.product_system.repository;

import de.neuefische.backend.product_system.model.ProductBody;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<ProductBody, String> {
}
