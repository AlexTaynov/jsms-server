package ru.jsms.backend.articles.repository;

import org.springframework.stereotype.Repository;
import ru.jsms.backend.articles.entity.Author;
import ru.jsms.backend.common.repository.BaseOwneredRepository;

@Repository
public interface AuthorRepository extends BaseOwneredRepository<Author, Long> {
}
