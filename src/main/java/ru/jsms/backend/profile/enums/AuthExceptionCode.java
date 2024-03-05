package ru.jsms.backend.profile.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.jsms.backend.common.exception.ApiException;

import java.text.MessageFormat;

@Getter
@AllArgsConstructor
public enum AuthExceptionCode {
    ACCOUNT_NOT_FOUND("account_not_found", "Пользователь не найден", HttpStatus.NOT_FOUND),
    WRONG_PASSWORD("wrong_password", "Неправильный пароль", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID("token_invalid", "Невалидный токен", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS("email_already_exists", "Пользователь с таким email уже существует", HttpStatus.BAD_REQUEST),
    EMAIL_CODE_INVALID("email_code_invalid", "Код невалидный или истек срок действия", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_CONFIRMED("email_already_confirmed", "Почта уже подтверждена", HttpStatus.BAD_REQUEST);

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
