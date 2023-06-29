
package de.neuefische.backend.usersystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRegistrationDTO {
    private String username;
    private String password;
    private List<String> authorities;
}