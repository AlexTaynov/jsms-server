package ru.jsms.backend.admin.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.jsms.backend.admin.entity.OfferArticleAnswer;
import ru.jsms.backend.common.repository.BaseRepository;

import java.util.Optional;

@Repository
public interface OfferArticleAnswerRepository extends BaseRepository<OfferArticleAnswer, Long> {
    @Transactional(readOnly = true)
    @Query("select a from OfferArticleAnswer a where a.versionId = ?1 and a.deleted = false")
    Optional<OfferArticleAnswer> findByVersionId(Long versionId);
}
