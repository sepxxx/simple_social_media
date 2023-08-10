package com.simple_social_media.controllers;

import com.simple_social_media.dtos.responses.UserResponse;
import com.simple_social_media.entities.User;
import com.simple_social_media.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@SecurityRequirement(name="bearerAuth")
public class UserController {
    private final UserService userService;



    @Operation(summary = "Get all users(only admin can request)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all users",
                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = UserResponse.class)
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                    ) }),
            @ApiResponse(responseCode = "403", description = "Not an admin",
                    content = @Content) })
    @GetMapping("")
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    @GetMapping("{id}/posts")
    public ResponseEntity<?> getUserPostsByUserId(@PathVariable Long id) {
        return userService.getUserPostsByUserId(id);
    }

}