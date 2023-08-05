package com.simple_social_media.dtos.responses;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConversationResponse {
    private Long conversationId;
    private Long uid1;
    private Long uid2;

}
