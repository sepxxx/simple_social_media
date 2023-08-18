package com.SocialMediaApi.controllers;

import com.SocialMediaApi.dtos.requests.JwtRequest;
import com.SocialMediaApi.dtos.requests.UserRegistrationRequest;
import com.SocialMediaApi.dtos.responses.JwtResponse;
import com.SocialMediaApi.dtos.responses.UserResponse;
import com.SocialMediaApi.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthAndRegController {

    private final AuthService authService;

    @Operation(summary = "Register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful registration",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    ) }),
            @ApiResponse(responseCode = "400", description = "Username/Email already exists",
                    content = @Content)
    })
    @PostMapping("/reg")
    public ResponseEntity<?> createNewUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        return authService.createNewUser(userRegistrationRequest);
    }


    @Operation(summary = "Authenticate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful authentication",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class)
                    ) }),
            @ApiResponse(responseCode = "400", description = "Username/Password is/are incorrect",
                    content = @Content)
    })
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest) {
        return authService.createAuthToken(jwtRequest);
    }

}
