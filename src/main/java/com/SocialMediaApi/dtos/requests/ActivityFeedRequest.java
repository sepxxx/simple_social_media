package com.SocialMediaApi.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityFeedRequest {
    private Integer limit;
    private Integer page;
}
