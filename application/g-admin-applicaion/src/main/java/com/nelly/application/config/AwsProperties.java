package com.nelly.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cloud.aws")
@Data
public class AwsProperties {
    private final S3 s3 = new S3();

    @Data
    public static class S3 {
        private String bucket;
        private String directory;
    }
}
