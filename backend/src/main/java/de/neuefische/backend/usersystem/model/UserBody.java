package de.neuefische.backend.usersystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("UserList")
public class UserBody {
    private String id;
    @Id
    private String username;
    private String password;
    private List<SimpleGrantedAuthority> authorities;
}
