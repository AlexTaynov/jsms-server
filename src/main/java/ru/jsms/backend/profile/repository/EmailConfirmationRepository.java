package ru.jsms.backend.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.jsms.backend.profile.entity.EmailConfirmation;

import java.util.Optional;
import java.util.UUID;

public interface EmailConfirmationRepository extends JpaRepository<EmailConfirmation, String> {
    Optional<EmailConfirmation> findByCode(UUID code);
}