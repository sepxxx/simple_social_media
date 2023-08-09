package com.simple_social_media.dtos.requests;

import lombok.Data;

@Data
public class ActivityFeedRequest {
    private Integer limit;
    private Integer page;
}
