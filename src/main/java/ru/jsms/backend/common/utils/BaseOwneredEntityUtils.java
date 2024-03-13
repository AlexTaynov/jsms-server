package ru.jsms.backend.common.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.jsms.backend.common.entity.BaseOwneredEntity;
import ru.jsms.backend.profile.service.AuthService;

import static ru.jsms.backend.common.enums.CommonExceptionCode.ACCESS_DENIED;

@Component
@AllArgsConstructor
public class BaseOwneredEntityUtils {

    private static AuthService authService;

    public static void validateAccess(BaseOwneredEntity<?> entity) {
        final Long userId = authService.getUserId();
        if (!entity.getOwnerId().equals(userId)) {
            throw ACCESS_DENIED.getException();
        }
    }
}
