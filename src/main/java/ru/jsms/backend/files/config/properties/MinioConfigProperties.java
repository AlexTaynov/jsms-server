package ru.jsms.backend.files.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "minio")
public class MinioConfigProperties {
    private String url;
    private String username;
    private String password;
    private String bucket;
}
