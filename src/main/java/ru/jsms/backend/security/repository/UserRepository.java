package ru.jsms.backend.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jsms.backend.security.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
