package ru.jsms.backend.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jsms.backend.security.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
