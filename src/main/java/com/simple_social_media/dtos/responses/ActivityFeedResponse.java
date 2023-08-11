package com.simple_social_media.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ActivityFeedResponse {
    private List<PostResponse> posts;
    private Integer pagesAmount;
}
