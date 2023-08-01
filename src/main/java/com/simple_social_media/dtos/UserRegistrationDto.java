package com.simple_social_media.dtos;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String username;
    private String email;
    private String password;
}
