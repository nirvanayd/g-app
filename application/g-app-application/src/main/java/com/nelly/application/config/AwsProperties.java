package com.nelly.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cloud.aws")
@Data
public class AwsProperties {
    private final AwsProperties.CloudFront cloudFront = new AwsProperties.CloudFront();
    private final AwsProperties.S3 s3 = new AwsProperties.S3();
    /**
     default-dir
     url: 'https://d3b7cshusafhca.cloudfront.net/'
     brand-dir: 'local/brand'
     default-image-url: 'https://d3b7cshusafhca.cloudfront.net/local/default/logo-default.png'
     content-dir: 'local/content'
     */
    @Data
    public static class CloudFront {
        private String defaultDir;
        private String url;
        private String brandDir;
        private String contentDir;
        private String defaultImageUrl;
    }

    @Data
    public static class S3 {
        private String bucket;
        private String directory;
    }
}
