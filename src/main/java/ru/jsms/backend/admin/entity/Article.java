package ru.jsms.backend.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.jsms.backend.admin.enums.ArticleStatus;
import ru.jsms.backend.common.entity.BaseEntity;
import ru.jsms.backend.user.entity.OfferArticle;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
public class Article extends BaseEntity<Long> {

    @OneToOne
    @JoinColumn(name = "offer_article_id")
    private OfferArticle offerArticle;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ArticleStatus status = ArticleStatus.NEW;

    private String comment;

}
