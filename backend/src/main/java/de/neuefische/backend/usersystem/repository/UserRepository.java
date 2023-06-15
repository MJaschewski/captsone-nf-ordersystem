package de.neuefische.backend.usersystem.repository;

import de.neuefische.backend.usersystem.model.UserBody;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserBody, String> {
}
