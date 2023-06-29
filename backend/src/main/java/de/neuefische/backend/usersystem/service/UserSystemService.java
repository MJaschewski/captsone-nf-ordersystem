package de.neuefische.backend.usersystem.service;

import de.neuefische.backend.supportsystem.service.GenerateIdService;
import de.neuefische.backend.usersystem.model.LoginDTO;
import de.neuefische.backend.usersystem.model.UserBody;
import de.neuefische.backend.usersystem.model.UserRegistrationDTO;
import de.neuefische.backend.usersystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSystemService implements UserDetailsService {

    private final UserRepository userRepository;
    private final GenerateIdService generateIdService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserBody userBody = userRepository.findUserBodyByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + username + " not found"));
        return new User(userBody.getUsername(), userBody.getPassword(), userBody.getRoles());
    }

    public LoginDTO saveUser(UserRegistrationDTO userDTO) {
        UserBody userBody = new UserBody();

        PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

        userBody.setUsername(userDTO.getUsername());
        userBody.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userBody.setId(generateIdService.generateUserUUID());
        userBody.setRoles(userDTO.getRoles());

        userRepository.save(userBody);

        return new LoginDTO(userBody.getUsername(), userBody.getRoles().stream().map(Object::toString).toList());

    }
}
