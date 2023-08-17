package com.SocialMediaApi.dtos.requests;

import lombok.Data;

@Data
public class MessageRequest {
    private Long targetUserId;
    private String text;
}
