package ru.jsms.backend.common.dto;

import lombok.Data;

@Data
public class ErrorDto {
    private String id;
    private String code;
    private String message;
    private String description;
}
