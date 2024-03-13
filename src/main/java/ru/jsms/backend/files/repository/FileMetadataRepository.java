package ru.jsms.backend.files.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.jsms.backend.common.repository.BaseOwneredRepository;
import ru.jsms.backend.files.entity.FileMetadataEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileMetadataRepository extends BaseOwneredRepository<FileMetadataEntity, Long> {

    @Transactional(readOnly = true)
    @Query("select f from FileMetadataEntity f where f.uuid = ?1 and f.deleted = false")
    Optional<FileMetadataEntity> findByUuid(UUID uuid);
}
