package com.SocialMediaApi.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageRequest {
    private Long targetUserId;
    private String text;
}
