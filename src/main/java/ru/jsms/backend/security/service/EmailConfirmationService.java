package ru.jsms.backend.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.jsms.backend.security.entity.EmailConfirmation;
import ru.jsms.backend.security.entity.UserData;
import ru.jsms.backend.security.repository.EmailConfirmationRepository;
import ru.jsms.backend.security.repository.UserDataRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static ru.jsms.backend.security.enums.AuthExceptionCode.EMAIL_ALREADY_CONFIRMED;
import static ru.jsms.backend.security.enums.AuthExceptionCode.EMAIL_CODE_INVALID;

@Service
@RequiredArgsConstructor
public class EmailConfirmationService {

    @Value("${email.verification.ttl}")
    private int ttl;

    private final EmailConfirmationRepository emailConfirmationRepository;
    private final UserDataRepository userDataRepository;
    private final NotificationService notificationService;

    public void sendCode(Long userId) {
        UserData userData = userDataRepository.findById(userId).get();
        emailConfirmationRepository.findById(userData.getEmail())
                .map(EmailConfirmation::isConfirmed)
                .filter(isConfirmed -> isConfirmed)
                .ifPresent(s -> {
                    throw EMAIL_ALREADY_CONFIRMED.getException();
                });
        EmailConfirmation emailConfirmation = EmailConfirmation.builder()
                .email(userData.getEmail())
                .code(UUID.randomUUID())
                .expiryDate(Instant.now().plus(ttl, ChronoUnit.MINUTES))
                .confirmed(false)
                .build();
        emailConfirmationRepository.save(emailConfirmation);
        notificationService.sendEmailConfirmationCode(emailConfirmation);
    }

    public void confirm(UUID code) {
        EmailConfirmation emailConfirmation = emailConfirmationRepository.findByCode(code)
                .orElseThrow(EMAIL_CODE_INVALID.getException());
        if (emailConfirmation.getExpiryDate().isBefore(Instant.now())) {
            throw EMAIL_CODE_INVALID.getException();
        }
        emailConfirmation.setConfirmed(true);
        emailConfirmationRepository.save(emailConfirmation);
    }
}
