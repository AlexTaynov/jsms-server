package ru.jsms.backend.common.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import ru.jsms.backend.common.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity<ID>, ID extends Serializable>
        extends CrudRepository<T, ID> {

    @Override
    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.id = ?1 and e.deleted = false")
    Optional<T> findById(ID id);

    @Override
    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.deleted = false")
    List<T> findAll();

    @Override
    @Transactional(readOnly = true)
    @Query("select e from #{#entityName} e where e.id in ?1 and e.deleted = false")
    Iterable<T> findAllById(Iterable<ID> ids);

    //Look up deleted entities
    @Query("select e from #{#entityName} e where e.deleted = true")
    @Transactional(readOnly = true)
    List<T> findDeleted();

    @Override
    @Transactional(readOnly = true)
    @Query("select count(e) from #{#entityName} e where e.deleted = false")
    long count();

    @Override
    @Transactional(readOnly = true)
    default boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    @Override
    @Query("update #{#entityName} e set e.deleted = false where e.id = ?1")
    @Transactional
    @Modifying
    void deleteById(ID id);


    @Override
    @Transactional
    default void delete(T entity) {
        deleteById(entity.getId());
    }

    @Override
    @Transactional
    default void deleteAll(Iterable<? extends T> entities) {
        entities.forEach(entity -> deleteById(entity.getId()));
    }

    @Override
    @Query("update #{#entityName} e set e.deleted=true")
    @Transactional
    @Modifying
    void deleteAll();
}