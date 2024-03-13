package ru.jsms.backend.common.utils;

import ru.jsms.backend.common.entity.BaseOwneredEntity;
import ru.jsms.backend.profile.service.AuthService;

import static ru.jsms.backend.common.enums.CommonExceptionCode.ACCESS_DENIED;

public class BaseOwneredEntityUtils {

    public static void validateAccess(BaseOwneredEntity<?> entity) {
        final Long userId = AuthService.getUserId();
        if (!entity.getOwnerId().equals(userId)) {
            throw ACCESS_DENIED.getException();
        }
    }
}
