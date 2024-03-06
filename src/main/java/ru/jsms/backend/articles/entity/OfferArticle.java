package ru.jsms.backend.articles.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.jsms.backend.articles.enums.OfferArticleStatus;
import ru.jsms.backend.common.entity.BaseOwneredEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "offer_article")
public class OfferArticle extends BaseOwneredEntity<Long> {

    private String name;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OfferArticleStatus status = OfferArticleStatus.DRAFT;

}
