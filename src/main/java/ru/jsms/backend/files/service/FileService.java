package ru.jsms.backend.files.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.jsms.backend.common.utils.BaseOwneredEntityUtils;
import ru.jsms.backend.files.dto.FileDto;
import ru.jsms.backend.files.entity.FileMetadataEntity;
import ru.jsms.backend.files.repository.FileMetadataRepository;
import ru.jsms.backend.profile.service.AuthService;

import java.util.UUID;

import static ru.jsms.backend.files.enums.FileExceptionCode.FILE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FileService {

    private final StorageService storageService;
    private final FileMetadataRepository fileMetadataRepository;

    public FileDto save(MultipartFile file) {
        Long userId = AuthService.getUserId();
        FileMetadataEntity fileMetadata = FileMetadataEntity.builder()
                .uuid(UUID.randomUUID())
                .name(file.getOriginalFilename())
                .size(file.getSize())
                .ownerId(userId)
                .build();
        fileMetadataRepository.save(fileMetadata);
        storageService.save(file, fileMetadata.getUuid());
        return new FileDto(fileMetadata.getUuid(), fileMetadata.getName());
    }

    public String getFilename(UUID uuid) {
        FileMetadataEntity fileMetadata = fileMetadataRepository.findByUuid(uuid)
                .orElseThrow(FILE_NOT_FOUND.getException());
        return fileMetadata.getName();
    }

    public Resource getFileResource(UUID uuid) {
        validateAccess(uuid);
        return new InputStreamResource(storageService.getInputStream(uuid));
    }

    private void validateAccess(UUID uuid) {
        if (AuthService.isAdmin())
            return;
        FileMetadataEntity fileMetadata = fileMetadataRepository.findByUuid(uuid)
                .orElseThrow(FILE_NOT_FOUND.getException());
        BaseOwneredEntityUtils.validateAccess(fileMetadata);
    }
}
