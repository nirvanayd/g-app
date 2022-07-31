package com.nelly.application.dto.response;

import lombok.Data;

@Data
public class AppVersionResponse {
    private String appVersion;
    private String minVersion;
    private String latestVersion;
}
