package ru.jsms.backend.profile.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.jsms.backend.profile.entity.EmailConfirmation;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender mailSender;

    public void sendEmailConfirmationCode(EmailConfirmation emailConfirmation) {
        SimpleMailMessage message = buildEmailConfirmationMessage(emailConfirmation);
        log.info("На почту {} отправлено письмо: {}", message.getTo(), message.getText());
        mailSender.send(message);
    }

    private SimpleMailMessage buildEmailConfirmationMessage(EmailConfirmation emailConfirmation) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(emailConfirmation.getEmail());
        simpleMailMessage.setSubject("Подтверждение почты");
        simpleMailMessage.setText(
                "Код подтверждения: " + emailConfirmation.getCode() + "\n" +
                "Срок действия: " + emailConfirmation.getExpiryDate()
        );
        return simpleMailMessage;
    }
}
