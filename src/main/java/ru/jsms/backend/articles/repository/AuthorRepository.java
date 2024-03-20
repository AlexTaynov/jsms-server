package ru.jsms.backend.articles.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.jsms.backend.articles.entity.Author;
import ru.jsms.backend.common.repository.BaseRepository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends BaseRepository<Author, Long> {
    @Transactional(readOnly = true)
    @Query("select a from Author a where a.email = ?1 and a.deleted = false")
    Optional<Author> findByEmail(String email);

    @Transactional(readOnly = true)
    @Query("""
            select a from Author a
            where concat(a.secondName, ' ', a.firstName, ' ', coalesce(a.patronymic, '')) like %?1%
            and a.deleted = false
            """)
     Page<Author> findByFullnameLike(String fullnameSubstring, Pageable pageable);
}
