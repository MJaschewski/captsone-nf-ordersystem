package de.neuefische.backend.usersystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("UserList")
public class UserBody {
    private String id;
    private String username;
    private String password;
    private List<String> roles;
}
