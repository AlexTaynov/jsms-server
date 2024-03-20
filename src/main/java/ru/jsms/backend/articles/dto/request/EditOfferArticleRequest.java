package ru.jsms.backend.articles.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class EditOfferArticleRequest {
    private String name;
    private List<Long> authors;
}
