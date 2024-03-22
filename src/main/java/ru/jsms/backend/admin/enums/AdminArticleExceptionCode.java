package ru.jsms.backend.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.jsms.backend.common.exception.ApiException;

import java.text.MessageFormat;

@Getter
@AllArgsConstructor
public enum AdminArticleExceptionCode {
    ARTICLE_NOT_FOUND("article_not_found", "Статья не найдена", HttpStatus.NOT_FOUND),
    ANSWER_NOT_FOUND("answer_not_found", "Ответ не найден", HttpStatus.BAD_REQUEST),
    ANSWER_EDIT_DENIED("answer_edit_denied", "Ответ уже отправлен", HttpStatus.BAD_REQUEST),
    ANSWER_NOT_COMPLETE("answer_not_complete", "Должно быть заполнено хотя бы одно поле ответа",
            HttpStatus.BAD_REQUEST)
    ;

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
