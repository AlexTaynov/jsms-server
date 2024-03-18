package ru.jsms.backend.articles.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.jsms.backend.common.entity.BaseOwneredEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Author extends BaseOwneredEntity<Long> {

    @NotBlank
    private String firstName;

    @NotBlank
    private String secondName;

    private String patronymic;

    @NotNull
    @Email
    private String email;

    @ManyToMany(mappedBy = "authors")
    private Set<OfferArticle> articles;
}
