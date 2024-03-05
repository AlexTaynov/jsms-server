package ru.jsms.backend.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jsms.backend.profile.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
