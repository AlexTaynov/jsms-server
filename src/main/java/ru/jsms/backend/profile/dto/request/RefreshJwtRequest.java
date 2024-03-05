package ru.jsms.backend.profile.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RefreshJwtRequest {
    @NotBlank
    public String refreshToken;
}
