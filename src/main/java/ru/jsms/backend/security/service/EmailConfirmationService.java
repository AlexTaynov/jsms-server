package ru.jsms.backend.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jsms.backend.security.entity.EmailConfirmation;
import ru.jsms.backend.security.entity.UserData;
import ru.jsms.backend.security.repository.EmailConfirmationRepository;
import ru.jsms.backend.security.repository.UserDataRepository;

import java.time.Instant;
import java.time.Period;
import java.util.UUID;

import static ru.jsms.backend.security.enums.AuthExceptionCode.ACCOUNT_NOT_FOUND;
import static ru.jsms.backend.security.enums.AuthExceptionCode.EMAIL_CODE_EXPIRED;
import static ru.jsms.backend.security.enums.AuthExceptionCode.EMAIL_CODE_INVALID;

@Service
@RequiredArgsConstructor
public class EmailConfirmationService {

    private final EmailConfirmationRepository emailConfirmationRepository;
    private final UserDataRepository userDataRepository;
    private final NotificationService notificationService;

    public void sendCode(Long userId) {
        UserData userData = userDataRepository.findById(userId).orElseThrow(ACCOUNT_NOT_FOUND.getException());
        EmailConfirmation emailConfirmation = EmailConfirmation.builder()
                .email(userData.getEmail())
                .code(UUID.randomUUID())
                .expiryDate(Instant.now().plus(Period.ofDays(1)))
                .confirmed(false)
                .build();
        emailConfirmationRepository.save(emailConfirmation);
        notificationService.sendEmailConfirmationCode(emailConfirmation);
    }

    public void confirm(UUID code) {
        EmailConfirmation emailConfirmation = emailConfirmationRepository.findByCode(code)
                .orElseThrow(EMAIL_CODE_INVALID.getException());
        if (emailConfirmation.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw EMAIL_CODE_EXPIRED.getException();
        }
        emailConfirmation.setConfirmed(true);
        emailConfirmationRepository.save(emailConfirmation);
    }
}
