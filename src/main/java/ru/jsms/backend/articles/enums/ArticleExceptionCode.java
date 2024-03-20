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
    EDIT_DENIED("delete_denied", "Заявка уже отправлена", HttpStatus.FORBIDDEN),
    ARTICLE_NOT_FOUND("article_not_found", "Заявка не найдена", HttpStatus.NOT_FOUND),
    VERSION_NOT_FOUND("version_not_found", "Версия заявки не найдена", HttpStatus.NOT_FOUND),
    SINGLE_VERSION_DELETE("single_version_delete", "Нельзя удалить единственную версию заявки",
            HttpStatus.BAD_REQUEST),
    VERSION_NOT_COMPLETE("version_not_complete", "Необходимо приложить архив со статьей и документами",
            HttpStatus.BAD_REQUEST),
    VERSION_NOT_DIFFERENT("version_not_different", "Версия заявки не отличается от предыдущей",
            HttpStatus.BAD_REQUEST),
    DRAFT_ALREADY_EXISTS("draft_already_exists", "У заявки может быть только один черновик",
            HttpStatus.BAD_REQUEST),
    AUTHOR_NOT_FOUND("author_not_found", "Автор не найден", HttpStatus.NOT_FOUND),
    AUTHOR_DELETE_DENIED("author_delete_denied", "Нельзя удалить автора, у которого есть статьи",
            HttpStatus.FORBIDDEN),
    AUTHOR_ALREADY_EXISTS("author_already_exists", "Автор с таким email уже существует",
            HttpStatus.BAD_REQUEST),
    OFFER_NOT_COMPLETE("offer_not_complete", "У статьи должно быть название и хотя бы один автор",
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
