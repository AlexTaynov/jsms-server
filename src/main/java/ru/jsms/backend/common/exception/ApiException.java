package ru.jsms.backend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

@Getter
public class ApiException extends RuntimeException implements Supplier<ApiException> {
    private final HttpStatus httpStatus;
    private final String code;
    private final String description;

    public ApiException(String message, String description, HttpStatus httpStatus, String code) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
        this.description = description;
    }

    public ApiException(String message, String description, Throwable cause, HttpStatus httpStatus, String code) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("%s %s", description, code);
    }

    @Override
    public ApiException get() {
        return this;
    }
}
