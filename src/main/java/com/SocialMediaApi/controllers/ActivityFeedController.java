package com.SocialMediaApi.controllers;

import com.SocialMediaApi.dtos.requests.ActivityFeedRequest;
import com.SocialMediaApi.dtos.responses.ActivityFeedResponse;
import com.SocialMediaApi.dtos.responses.MessageResponse;
import com.SocialMediaApi.services.ActivityFeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name="bearerAuth")
public class ActivityFeedController {
    private final ActivityFeedService activityFeedService;



    @Operation(summary = "Get activity feed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of posts, Nof pages",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ActivityFeedResponse.class))
                    ) }),
            @ApiResponse(responseCode = "404", description = "Page was not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "limit,page must be greater than 0 ",
                    content = @Content)
    })
    @GetMapping("/feed")
    public ResponseEntity<?> getCurrentUserActivityFeed(@RequestBody ActivityFeedRequest activityFeedRequest) {
        return activityFeedService.getCurrentUserActivityFeed(activityFeedRequest);
    }
}
