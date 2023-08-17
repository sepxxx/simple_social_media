package com.SocialMediaApi.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MessageResponse {
    private Long id;
    private Long conversation_id;
    private Long author_id;
    private String text;
    private Date date;
}
