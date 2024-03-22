package ru.jsms.backend.files.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.jsms.backend.common.dto.HeadersDto;
import ru.jsms.backend.common.utils.BaseOwneredEntityUtils;
import ru.jsms.backend.files.dto.FileDto;
import ru.jsms.backend.files.entity.FileMetadataEntity;
import ru.jsms.backend.files.repository.FileMetadataRepository;
import ru.jsms.backend.profile.service.UserService;

import java.util.UUID;

import static ru.jsms.backend.common.utils.UuidUtils.parseUuid;
import static ru.jsms.backend.files.enums.FileExceptionCode.FILE_NOT_FOUND;
import static ru.jsms.backend.profile.enums.AuthExceptionCode.ACCOUNT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FileService {

    private final UserService userService;
    private final StorageService storageService;
    private final FileMetadataRepository fileMetadataRepository;
    private final HeadersDto headersDto;

    public FileDto save(MultipartFile file, Long ownerId) {
        if (ownerId == null || !headersDto.isAdmin()) {
            ownerId = headersDto.getUserId();
        }
        else if (userService.getById(ownerId).isEmpty()) {
            throw ACCOUNT_NOT_FOUND.getException();
        }
        FileMetadataEntity fileMetadata = FileMetadataEntity.builder()
                .uuid(UUID.randomUUID())
                .name(file.getOriginalFilename())
                .size(file.getSize())
                .ownerId(ownerId)
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
        validateAccess(uuid.toString());
        return new InputStreamResource(storageService.getInputStream(uuid));
    }

    public void validateAccess(String uuidString) {
        FileMetadataEntity fileMetadata;
        try {
            fileMetadata = fileMetadataRepository.findByUuid(parseUuid(uuidString)).get();
        } catch (Exception e) {
            throw FILE_NOT_FOUND.getException(e.getMessage());
        }
        if (headersDto.isAdmin())
            return;
        BaseOwneredEntityUtils.validateAccess(fileMetadata, headersDto.getUserId());
    }

    public void validateAccess(UUID uuid) {
        validateAccess(uuid.toString());
    }
}
