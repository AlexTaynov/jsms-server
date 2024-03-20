package ru.jsms.backend.articles.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OfferArticleFullResponse {
    private Long id;
    private String name;
    private String status;
    private List<AuthorResponse> authors;
}
