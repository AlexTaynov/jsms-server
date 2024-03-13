package ru.jsms.backend.files.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.jsms.backend.files.config.properties.MinioConfigProperties;

@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient(MinioConfigProperties properties) throws Exception {
        MinioClient client = MinioClient.builder()
                .endpoint(properties.getUrl())
                .credentials(properties.getUsername(), properties.getPassword())
                .build();
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(properties.getBucket()).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(properties.getBucket()).build());
        }
        return client;
    }
}
