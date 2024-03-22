package ru.jsms.backend.user.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class EditOfferArticleRequest {
    private String name;
    private Set<Long> authorIds;
}
