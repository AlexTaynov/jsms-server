package ru.jsms.backend.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jsms.backend.profile.entity.UserData;

import java.util.Optional;

public interface UserDataRepository extends JpaRepository<UserData, Long> {
    Optional<UserData> findByEmail(String email);
}
