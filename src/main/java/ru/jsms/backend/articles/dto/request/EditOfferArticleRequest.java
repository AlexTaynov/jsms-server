package ru.jsms.backend.articles.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EditOfferArticleRequest {
    @NotBlank
    private String name;
}
