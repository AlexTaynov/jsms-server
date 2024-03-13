package ru.jsms.backend.profile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.jsms.backend.profile.service.AuthService;
import ru.jsms.backend.profile.service.EmailConfirmationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailConfirmationController {

    private final AuthService authService;
    private final EmailConfirmationService emailConfirmationService;

    @PostMapping("/sendCode")
    public void sendEmailConfirmationCode() {
        final Long userId = authService.getUserId();
        emailConfirmationService.sendCode(userId);
    }

    @GetMapping("/confirm")
    public void confirmEmail(@RequestParam UUID code) {
        emailConfirmationService.confirm(code);
    }
}
