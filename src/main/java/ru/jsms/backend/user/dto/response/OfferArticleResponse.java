package ru.jsms.backend.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.jsms.backend.user.entity.OfferArticle;

@Data
@AllArgsConstructor
@Builder
public class OfferArticleResponse {
    private Long id;
    private String name;
    private String status;

    public OfferArticleResponse(OfferArticle offerArticle) {
        this.id = offerArticle.getId();
        this.name = offerArticle.getName();
        this.status = String.valueOf(offerArticle.getStatus());
    }
}
