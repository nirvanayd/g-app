package com.nelly.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.config")
@Data
public class EnvProperties {
    private final Activate activate = new Activate();

    @Data
    public static class Activate {
        private String onProfile;
    }
}
