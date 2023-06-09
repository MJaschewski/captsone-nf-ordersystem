package de.neuefische.backend.productsystem.repository;

import de.neuefische.backend.productsystem.model.ProductBody;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<ProductBody, String> {
}
