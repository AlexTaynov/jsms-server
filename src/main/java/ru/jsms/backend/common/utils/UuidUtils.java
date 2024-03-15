package ru.jsms.backend.common.utils;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;

import java.util.UUID;

import static ru.jsms.backend.common.enums.CommonExceptionCode.UUID_NOT_VALID;

public class UuidUtils {

    public static UUID parseUuid(String uuid) {
        if (StringUtils.isBlank(uuid)) {
            return null;
        }
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw UUID_NOT_VALID.getException();
        }
    }
}
