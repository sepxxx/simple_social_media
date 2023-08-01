package com.simple_social_media.controllers;

import com.simple_social_media.dtos.JwtRequestDto;
import com.simple_social_media.dtos.UserRegistrationDto;
import com.simple_social_media.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequestDto jwtRequestDto) {
        return authService.createAuthToken(jwtRequestDto);
    }

    @PostMapping("/reg")
    public ResponseEntity<?> createNewUser(@RequestBody UserRegistrationDto userRegistrationDto) {
            return authService.createNewUser(userRegistrationDto);
    }

}
