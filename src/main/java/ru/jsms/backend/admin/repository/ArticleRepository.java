package ru.jsms.backend.admin.repository;

import org.springframework.stereotype.Repository;
import ru.jsms.backend.admin.entity.Article;
import ru.jsms.backend.common.repository.BaseRepository;

@Repository
public interface ArticleRepository extends BaseRepository<Article, Long> {
}
