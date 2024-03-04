package ru.jsms.backend.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.jsms.backend.security.entity.EmailConfirmation;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender mailSender;

    public void sendEmailConfirmationCode(EmailConfirmation emailConfirmation) {
        SimpleMailMessage message = createMailMessage(emailConfirmation);
        mailSender.send(message);
        log.info("На почту {} отправлено письмо: {}", message.getTo(), message.getText());
    }

    private SimpleMailMessage createMailMessage(EmailConfirmation emailConfirmation) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(emailConfirmation.getEmail());
        simpleMailMessage.setSubject("Потверждение почты");
        simpleMailMessage.setText(
                "Код подтверждения: " + emailConfirmation.getCode() + "\n" +
                "Срок действия: " + emailConfirmation.getExpiryDate()
        );
        return simpleMailMessage;
    }
}
