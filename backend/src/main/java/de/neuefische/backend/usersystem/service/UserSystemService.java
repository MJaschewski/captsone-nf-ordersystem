package de.neuefische.backend.usersystem.service;

import de.neuefische.backend.usersystem.model.UserBody;
import de.neuefische.backend.usersystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSystemService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserBody userBody = userRepository.findUserBodyByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + username + " not found"));
        return new User(userBody.getUsername(), userBody.getPassword(), List.of());
    }
}
