package ru.jsms.backend.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.jsms.backend.profile.dto.request.RegisterRequest;
import ru.jsms.backend.common.dto.Role;
import ru.jsms.backend.profile.entity.User;
import ru.jsms.backend.profile.entity.UserData;
import ru.jsms.backend.profile.repository.UserDataRepository;
import ru.jsms.backend.profile.repository.UserRepository;

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
        user.setUserData(UserData.builder()
                .email(registerRequest.getEmail())
                .firstName(registerRequest.getFirstName())
                .secondName(registerRequest.getSecondName())
                .build()
        );
        return user;
    }

    public Optional<User> getByEmail(String email) {
        return userDataRepository.findByEmail(email).map(UserData::getUser);
    }
}