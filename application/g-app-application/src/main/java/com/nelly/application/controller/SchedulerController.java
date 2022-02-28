package com.nelly.application.controller;

import com.nelly.application.service.content.ContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerController {

    private final ContentService contentService;

    @Scheduled(fixedDelay = 10000)
    public void updateContentCounts() {
        contentService.scheduleContentLikes();
        contentService.scheduleContentMarks();
    }
}
