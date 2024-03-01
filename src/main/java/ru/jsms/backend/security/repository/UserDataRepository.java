package ru.jsms.backend.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jsms.backend.security.entity.UserData;

import java.util.Optional;

public interface UserDataRepository extends JpaRepository<UserData, Long> {
    Optional<UserData> findByEmail(String email);
}
