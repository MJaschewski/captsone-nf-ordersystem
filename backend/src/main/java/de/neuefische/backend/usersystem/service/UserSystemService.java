package de.neuefische.backend.usersystem.service;

import de.neuefische.backend.productsystem.model.AccessLevel;
import de.neuefische.backend.supportsystem.service.GenerateIdService;
import de.neuefische.backend.usersystem.model.LoginDTO;
import de.neuefische.backend.usersystem.model.PasswordChangeDTO;
import de.neuefische.backend.usersystem.model.UserBody;
import de.neuefische.backend.usersystem.model.UserRegistrationDTO;
import de.neuefische.backend.usersystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public void authorityLeadCheck(List<String> authorities) throws IllegalAccessException {
        if (!authorities.contains(AccessLevel.LEAD.toString())) {
            throw new IllegalAccessException("No authority lead.");
        }
    }

    public LoginDTO saveUser(List<String> authorities, UserRegistrationDTO userDTO) throws IllegalAccessException {
        authorityLeadCheck(authorities);
        if (userDTO.getPassword().length() < 8 || userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Password needs to be at least 8 digits long and username needs to be unique.");
        }
        if (userDTO.getUsername().length() < 8) {
            throw new IllegalArgumentException("Username needs to be at least 8 digits long and needs to be unique");
        }
        if (!userDTO.getAuthorities().stream().map(Object::toString).toList().contains(AccessLevel.ALL.toString())) {
            throw new IllegalArgumentException("User must have authority All.");
        }
        UserBody userBody = new UserBody();
        PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        userBody.setUsername(userDTO.getUsername());
        userBody.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userBody.setId(generateIdService.generateUserUUID());
        userBody.setRoles(userDTO.getAuthorities().stream().map(SimpleGrantedAuthority::new).toList());

        userRepository.save(userBody);

        return new LoginDTO(userBody.getUsername(), userBody.getRoles().stream().map(Object::toString).toList());

    }

    public List<LoginDTO> getAllUsers(List<String> authorities) throws IllegalAccessException {
        authorityLeadCheck(authorities);
        List<UserBody> userBodyList = userRepository.findAll();
        List<LoginDTO> userDTOList = new ArrayList<>();
        userBodyList.forEach(userBody -> {
            LoginDTO nextUser = new LoginDTO(userBody.getUsername(), userBody.getRoles().stream().map(Objects::toString).toList());
            userDTOList.add(nextUser);
        });
        return userDTOList;
    }

    public String changePassword(String username, PasswordChangeDTO passwordChangeDTO) throws IllegalAccessException {
        UserBody savedUser = userRepository.findUserBodyByUsername(username).orElseThrow();
        PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        if (!passwordEncoder.matches(passwordChangeDTO.getOldPassword(), savedUser.getPassword())) {
            throw new IllegalAccessException("Incorrect Password.");
        }
        if (passwordChangeDTO.getNewPassword().length() < 8) {
            throw new IllegalArgumentException("Password needs to be at least 8 digits long");
        }
        savedUser.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
        userRepository.save(savedUser);
        return "Password Changed";
    }

    public LoginDTO changeAuthorities(List<String> authorites, LoginDTO loginDTO) throws IllegalAccessException {
        authorityLeadCheck(authorites);
        UserBody savedUser = userRepository.findUserBodyByUsername(loginDTO.getUsername()).orElseThrow();
        if (!loginDTO.getAuthorities().contains("ALL")) {
            throw new IllegalArgumentException("Authorities must contain ALL");
        }
        savedUser.setRoles(loginDTO.getAuthorities().stream().map(SimpleGrantedAuthority::new).toList());
        userRepository.save(savedUser);
        return new LoginDTO(savedUser.getUsername(), savedUser.getRoles().stream().map(Object::toString).toList());
    }

    public String deleteUser(String usernameLead, String password, String username) throws IllegalAccessException {
        UserBody leadUser = userRepository.findUserBodyByUsername(usernameLead).orElseThrow();
        PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        if (!passwordEncoder.matches(password, leadUser.getPassword()) || !leadUser.getRoles().contains(new SimpleGrantedAuthority("LEAD"))) {
            throw new IllegalAccessException("Wrong password or no authority.");
        }
        userRepository.delete(userRepository.findUserBodyByUsername(username).orElseThrow());
        return "User " + username + " deleted.";
    }
}
