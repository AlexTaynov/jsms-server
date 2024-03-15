package ru.jsms.backend.common.utils;

import ru.jsms.backend.common.entity.BaseOwneredEntity;

import static ru.jsms.backend.common.enums.CommonExceptionCode.ACCESS_DENIED;

public class BaseOwneredEntityUtils {

    public static void validateAccess(BaseOwneredEntity<?> entity, Long userId) {
        if (!entity.getOwnerId().equals(userId)) {
            throw ACCESS_DENIED.getException();
        }
    }
}
