package ru.jsms.backend.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtils {

    public static String getXRequestIdHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("x-request-id")).orElse(UUID.randomUUID().toString());
    }
}
