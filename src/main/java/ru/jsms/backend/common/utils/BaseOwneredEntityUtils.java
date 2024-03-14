package ru.jsms.backend.common.utils;

import ru.jsms.backend.common.dto.HeadersDto;
import ru.jsms.backend.common.entity.BaseOwneredEntity;

import static ru.jsms.backend.common.enums.CommonExceptionCode.ACCESS_DENIED;

public class BaseOwneredEntityUtils {

    public static void validateAccess(BaseOwneredEntity<?> entity, HeadersDto headersDto) {
        if (!entity.getOwnerId().equals(headersDto.getUserId())) {
            throw ACCESS_DENIED.getException();
        }
    }
}
