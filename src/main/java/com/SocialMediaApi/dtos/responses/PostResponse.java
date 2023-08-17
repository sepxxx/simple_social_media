package com.SocialMediaApi.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String header;
    private String text;
    private Date date;
    private String usernameCreatedBy;
    private String imageUrl;
}
