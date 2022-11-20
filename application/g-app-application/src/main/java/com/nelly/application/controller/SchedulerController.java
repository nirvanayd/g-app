package com.nelly.application.controller;

import com.nelly.application.service.brand.BrandService;
import com.nelly.application.service.content.ContentService;
import com.nelly.application.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerController {

    private final ContentService contentService;
    private final BrandService brandService;
    private final UserService userService;

    @Scheduled(fixedDelay = 3000)
    public void updateContentCounts() {
        contentService.scheduleContentLikes();
        contentService.scheduleContentMarks();
    }

    @Scheduled(fixedDelay = 3000)
    public void updateFavoriteCounts() {
        brandService.scheduleBrandFavorite();
    }

    @Scheduled(fixedDelay = 3000)
    public void updateContentReplyCounts() {
        contentService.scheduleContentReply();
    }

    @Scheduled(fixedDelay = 3000)
    public void updateFollowCounts() {
        userService.scheduleFollow();
    }


}