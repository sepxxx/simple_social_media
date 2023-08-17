package com.SocialMediaApi.dtos.responses;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConversationResponse {
    private Long conversationId;
    private String header;

}
