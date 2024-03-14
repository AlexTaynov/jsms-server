package ru.jsms.backend.files.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class FileDto {
    private UUID fileId;
    private String fileName;
}
