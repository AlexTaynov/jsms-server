package ru.jsms.backend.user.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class CreateOfferArticleRequest {
    @NotBlank
    private String name;

    private Set<Long> authorIds;
}
