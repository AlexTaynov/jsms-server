package ru.jsms.backend.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.jsms.backend.common.dto.ErrorDto;
import ru.jsms.backend.common.exception.ApiException;
import ru.jsms.backend.common.utils.RequestUtils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class CommonExceptionHandler {
    private final HttpServletRequest request;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        log.error(ex.toString(), ex);
        final ErrorDto apiError = new ErrorDto();
        apiError.setId(RequestUtils.getXRequestIdHeader(this.request));
        apiError.setCode("internal_server_error");
        apiError.setMessage("Внутренняя ошибка");
        apiError.setDescription(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDto> handleApiException(ApiException ex) {
        log.error(ex.toString(), ex);
        final ErrorDto apiError = new ErrorDto();
        apiError.setId(RequestUtils.getXRequestIdHeader(this.request));
        apiError.setCode(ex.getCode());
        apiError.setMessage(ex.getMessage());
        apiError.setDescription(ex.getDescription());
        return new ResponseEntity<>(apiError, ex.getHttpStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleApiException(AccessDeniedException ex) {
        log.error(ex.toString(), ex);
        final ErrorDto apiError = new ErrorDto();
        apiError.setId(RequestUtils.getXRequestIdHeader(this.request));
        apiError.setCode("forbidden");
        apiError.setMessage("Доступ запрещен");
        apiError.setDescription(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }
}
