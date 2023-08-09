package com.simple_social_media.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostRequest {
    private String header;
    private String text;
    private String image_url;
}
