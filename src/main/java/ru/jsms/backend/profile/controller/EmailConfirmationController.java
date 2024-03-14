package ru.jsms.backend.profile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.jsms.backend.common.dto.HeadersDto;
import ru.jsms.backend.profile.service.EmailConfirmationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailConfirmationController {

    private final EmailConfirmationService emailConfirmationService;
    private final HeadersDto headersDto;

    @PostMapping("/sendCode")
    public void sendEmailConfirmationCode() {
        emailConfirmationService.sendCode(headersDto.getUserId());
    }

    @GetMapping("/confirm")
    public void confirmEmail(@RequestParam UUID code) {
        emailConfirmationService.confirm(code);
    }
}
