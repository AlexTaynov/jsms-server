package ru.jsms.backend.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import ru.jsms.backend.common.entity.BaseOwneredEntity;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseOwneredRepository<T extends BaseOwneredEntity<ID>, ID extends Serializable>
        extends BaseRepository<T, ID> {

    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.ownerId = ?1 and e.deleted = false")
    Page<T> findByOwnerId(Long ownerId, Pageable pageable);

}