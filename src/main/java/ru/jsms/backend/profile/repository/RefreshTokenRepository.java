package ru.jsms.backend.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jsms.backend.profile.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
