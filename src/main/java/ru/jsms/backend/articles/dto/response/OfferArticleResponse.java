package ru.jsms.backend.articles.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OfferArticleResponse {
    private Long id;
    private String name;
    private String status;
}
