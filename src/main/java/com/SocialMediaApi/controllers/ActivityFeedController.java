package com.SocialMediaApi.controllers;

import com.SocialMediaApi.dtos.requests.ActivityFeedRequest;
import com.SocialMediaApi.services.ActivityFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ActivityFeedController {
    private final ActivityFeedService activityFeedService;

    @GetMapping("/feed")
    public ResponseEntity<?> getCurrentUserActivityFeed(@RequestBody ActivityFeedRequest activityFeedRequest) {
        return activityFeedService.getCurrentUserActivityFeed(activityFeedRequest);
    }
}
