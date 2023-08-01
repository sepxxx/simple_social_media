package com.simple_social_media.dtos;

import lombok.Data;

@Data
public class JwtRequestDto {
    private String username;
    private String password;
}
