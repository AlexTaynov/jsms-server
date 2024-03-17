package ru.jsms.backend.profile.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Claims {
    USER_ID("userId");

    private final String name;
}
