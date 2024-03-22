package ru.jsms.backend.admin.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.jsms.backend.admin.entity.Review;
import ru.jsms.backend.common.repository.BaseRepository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends BaseRepository<Review, Long> {
    @Transactional(readOnly = true)
    @Query("select r from Review r where r.versionId = ?1 and r.deleted = false")
    Optional<Review> findByVersionId(Long versionId);
}
