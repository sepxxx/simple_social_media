package com.simple_social_media.controllers;

import com.simple_social_media.dtos.requests.JwtRequest;
import com.simple_social_media.dtos.requests.UserRegistrationRequest;
import com.simple_social_media.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

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
