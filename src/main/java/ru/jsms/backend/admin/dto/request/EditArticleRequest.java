package ru.jsms.backend.admin.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditArticleRequest {
    private String status;
    private String comment;
}
