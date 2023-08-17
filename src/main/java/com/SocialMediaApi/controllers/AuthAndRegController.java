package com.SocialMediaApi.controllers;

import com.SocialMediaApi.dtos.requests.JwtRequest;
import com.SocialMediaApi.dtos.requests.UserRegistrationRequest;
import com.SocialMediaApi.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthAndRegController {

    private final AuthService authService;

    @PostMapping("/reg")
    public ResponseEntity<?> createNewUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        return authService.createNewUser(userRegistrationRequest);
    }
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest) {
        return authService.createAuthToken(jwtRequest);
    }

}
