package ru.jsms.backend.articles.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.jsms.backend.articles.enums.OfferArticleStatus;
import ru.jsms.backend.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "offer_article")
public class OfferArticle extends BaseEntity<Long> {

    @Column(name = "owner_id")
    private Long ownerId;

    private String name;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OfferArticleStatus status = OfferArticleStatus.DRAFT;

}
