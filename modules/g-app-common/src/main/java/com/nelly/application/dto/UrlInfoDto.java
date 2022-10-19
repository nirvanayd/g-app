package com.nelly.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UrlInfoDto {
    private String protocol;
    private String authority;
    private String host;
    private Integer port;
    private String path;
    private String query;
    private String file;
    private String ref;
}
