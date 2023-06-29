package de.neuefische.backend.usersystem.repository;

import de.neuefische.backend.usersystem.model.UserBody;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserBody, String> {

    public Optional<UserBody> findUserBodyByUsername(String username);

    public Boolean existsByUsername(String username);
}
