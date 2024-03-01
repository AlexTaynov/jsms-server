package ru.jsms.backend.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.jsms.backend.security.domain.RegisterRequest;
import ru.jsms.backend.security.domain.Role;
import ru.jsms.backend.security.entity.User;
import ru.jsms.backend.security.entity.UserData;
import ru.jsms.backend.security.repository.UserDataRepository;
import ru.jsms.backend.security.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDataRepository userDataRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(RegisterRequest registerRequest, Set<Role> roles) {
        String passwordEncoded = passwordEncoder.encode(registerRequest.getPassword());
        User user = userRepository.save(new User(passwordEncoded, roles));
        user.setUserData(
                new UserData(registerRequest.getEmail(), registerRequest.getFirstName(), registerRequest.getSecondName())
        );
        return user;
    }

    public Optional<User> getByEmail(String email) {
        Optional<UserData> userData = userDataRepository.findByEmail(email);
        return userData.map(UserData::getUser);
    }
}