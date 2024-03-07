package ru.jsms.backend.articles.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateOfferArticleRequest {
    @NotBlank
    private String name;
}
