package ru.jsms.backend.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class RefreshTokenDto {
    private Long userId;
    private String token;
    private Instant refreshExpirationInstant;
}
