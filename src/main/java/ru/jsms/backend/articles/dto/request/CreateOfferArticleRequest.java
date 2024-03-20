package ru.jsms.backend.articles.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CreateOfferArticleRequest {
    @NotBlank
    private String name;

    private List<Long> authors;
}
