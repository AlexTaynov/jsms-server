package ru.jsms.backend.articles.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.jsms.backend.common.exception.ApiException;

import java.text.MessageFormat;

@Getter
@AllArgsConstructor
public enum ArticleExceptionCode {
    ACCESS_DENIED("access_denied", "Доступ запрещен", HttpStatus.FORBIDDEN),
    EDIT_DENIED("edit_denied", "Разрешено удалять или редактировать заявку только со статусом DRAFT",
            HttpStatus.FORBIDDEN);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;

    public ApiException getException(String description, Throwable ex) {
        return new ApiException(description, description, ex, httpStatus, errorCode);
    }

    public ApiException getException(String description) {
        return new ApiException(message, description, httpStatus, errorCode);
    }

    public ApiException getException() {
        return new ApiException(message, message, httpStatus, errorCode);
    }

    public ApiException getExceptionWithParams(Object... params) {
        return new ApiException(formatMessage(message, params), formatMessage(message, params), httpStatus, errorCode);
    }

    public static String formatMessage(String message, Object... params) {
        if (params != null) {
            return MessageFormat.format(message, params);
        } else {
            return message;
        }
    }
}
