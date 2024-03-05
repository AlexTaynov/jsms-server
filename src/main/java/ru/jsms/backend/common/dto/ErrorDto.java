package ru.jsms.backend.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {
    private String id;
    private String code;
    private String message;
    private String description;
}
