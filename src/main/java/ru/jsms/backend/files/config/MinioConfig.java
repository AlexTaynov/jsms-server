package ru.jsms.backend.files.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.username}")
    private String minioUsername;

    @Value("${minio.password}")
    private String minioPassword;

    @Value("${minio.bucket}")
    private String minioBucket;

    @Bean
    public MinioClient minioClient() throws Exception {
        MinioClient client = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(minioUsername, minioPassword)
                .build();
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(minioBucket).build())) {
            client.makeBucket(
                    MakeBucketArgs
                            .builder()
                            .bucket(minioBucket)
                            .build()
            );
        }
        return client;
    }
}
