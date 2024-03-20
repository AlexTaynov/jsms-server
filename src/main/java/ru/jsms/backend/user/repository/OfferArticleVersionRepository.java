package ru.jsms.backend.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.jsms.backend.user.entity.OfferArticleVersion;
import ru.jsms.backend.common.repository.BaseOwneredRepository;

import java.util.Optional;

@Repository
public interface OfferArticleVersionRepository extends BaseOwneredRepository<OfferArticleVersion, Long> {

    @Transactional(readOnly = true)
    @Query("select o from OfferArticleVersion o where o.offerArticle.id = ?1 and o.ownerId = ?2 and o.deleted = false")
    Page<OfferArticleVersion> findByOfferArticleIdAndOwnerId(Long offerArticleId, Long userId, Pageable pageable);

    @Transactional(readOnly = true)
    @Query(value = """
            select * from offer_article_version o
            where o.offer_article_id = ?1
            and o.deleted = false
            order by o.created desc limit 1
            """, nativeQuery = true)
    OfferArticleVersion findLastVersionByOfferArticleId(Long offerArticleId);

    @Transactional(readOnly = true)
    @Query("select count(o) from OfferArticleVersion o where o.offerArticle.id = ?1 and o.deleted = false")
    Long countByOfferArticleId(Long offerArticleId);

    @Transactional(readOnly = true)
    @Query(value = """
            select * from offer_article_version o
            where o.offer_article_id = ?1
            and o.deleted = false
            and o.is_draft = false
            order by o.created desc limit 1
            """, nativeQuery = true)
    Optional<OfferArticleVersion> findLastSubmittedVersion(Long offerArticleId);
}
