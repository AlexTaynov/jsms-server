package ru.jsms.backend.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.jsms.backend.security.entity.EmailConfirmation;

@Service
@Slf4j
public class NotificationService {
    public void sendEmailConfirmationCode(EmailConfirmation emailConfirmation) {
        log.info("Send code {} to {}", emailConfirmation.getCode(), emailConfirmation.getEmail());
    }
}
