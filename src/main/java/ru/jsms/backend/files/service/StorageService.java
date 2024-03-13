package ru.jsms.backend.files.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.jsms.backend.files.config.properties.MinioConfigProperties;

import java.io.InputStream;
import java.util.UUID;

import static ru.jsms.backend.files.enums.FileExceptionCode.STORAGE_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    private final MinioConfigProperties properties;
    private final MinioClient minioClient;

    public void save(MultipartFile file, UUID uuid) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(properties.getBucket())
                            .object(uuid.toString())
                            .stream(file.getInputStream(), file.getSize(), 0)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw STORAGE_ERROR.getException();
        }
    }

    public InputStream getInputStream(UUID uuid) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder().bucket(properties.getBucket()).object(uuid.toString()).build());
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw STORAGE_ERROR.getException();
        }
    }

}
