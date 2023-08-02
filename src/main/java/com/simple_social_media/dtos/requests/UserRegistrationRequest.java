package com.simple_social_media.dtos.requests;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String username;
    private String email;
    private String password;
}
