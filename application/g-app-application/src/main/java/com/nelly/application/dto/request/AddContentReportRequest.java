package com.nelly.application.dto.request;

import lombok.Data;

@Data
public class AddContentReportRequest {
    private Long contentId;
    private Long reportId;
    private String text;
}