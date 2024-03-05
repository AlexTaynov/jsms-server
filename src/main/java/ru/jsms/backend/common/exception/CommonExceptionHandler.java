package ru.jsms.backend.common.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.jsms.backend.common.dto.ErrorDto;
import ru.jsms.backend.common.exception.ApiException;
import ru.jsms.backend.common.utils.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class CommonExceptionHandler {
    private final HttpServletRequest request;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        log.error(ex.toString(), ex);
        final ErrorDto apiError = ErrorDto.builder()
                .id(RequestUtils.getXRequestIdHeader(this.request))
                .code("internal_server_error")
                .message("Внутренняя ошибка")
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationException(MethodArgumentNotValidException ex) {
        log.error(ex.toString(), ex);
        final ErrorDto apiError = ErrorDto.builder()
                .id(RequestUtils.getXRequestIdHeader(this.request))
                .code("validation_error")
                .message("Ошибка валидации")
                .description(buildDescription(ex))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDto> handleApiException(ApiException ex) {
        log.error(ex.toString(), ex);
        final ErrorDto apiError = ErrorDto.builder()
                .id(RequestUtils.getXRequestIdHeader(this.request))
                .code(ex.getCode())
                .message(ex.getMessage())
                .description(ex.getDescription())
                .build();
        return new ResponseEntity<>(apiError, ex.getHttpStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleApiException(AccessDeniedException ex) {
        log.error(ex.toString(), ex);
        final ErrorDto apiError = ErrorDto.builder()
                .id(RequestUtils.getXRequestIdHeader(this.request))
                .code("forbidden")
                .message("Доступ запрещен")
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    private String buildDescription(MethodArgumentNotValidException ex) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        return errors.stream().map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
    }
}
