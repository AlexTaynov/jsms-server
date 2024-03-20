package ru.jsms.backend.articles.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.jsms.backend.articles.enums.OfferArticleStatus;
import ru.jsms.backend.common.entity.BaseOwneredEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

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

    @OneToMany(mappedBy = "offerArticle", cascade = CascadeType.ALL)
    private Set<OfferArticleVersion> versions;

    @ManyToMany
    @JoinTable(
            name = "article_authors",
            joinColumns = { @JoinColumn(name = "article_id") },
            inverseJoinColumns = { @JoinColumn(name = "author_id") }
    )
    private Set<Author> authors;

    public boolean isComplete() {
        return !isBlank(name) && !authors.isEmpty();
    }
}
