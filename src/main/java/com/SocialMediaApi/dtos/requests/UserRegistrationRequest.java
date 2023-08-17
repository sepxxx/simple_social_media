package com.SocialMediaApi.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegistrationRequest {
    private String username;
    private String email;
    private String password;
}
