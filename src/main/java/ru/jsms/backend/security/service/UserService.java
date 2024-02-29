package ru.jsms.backend.security.service;

import lombok.RequiredArgsConstructor;
import ru.jsms.backend.security.domain.Role;
import ru.jsms.backend.security.entity.User;
import org.springframework.stereotype.Service;
import ru.jsms.backend.security.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(Long id, String password, Set<Role> roles) {
        return userRepository.save(new User(id, password, roles));
    }
}