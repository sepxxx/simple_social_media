package com.simple_social_media.controllers;

import com.simple_social_media.dtos.requests.PostRequest;
import com.simple_social_media.dtos.responses.PostResponse;
import com.simple_social_media.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("posts")
@RequiredArgsConstructor
@SecurityRequirement(name="bearerAuth")
public class PostController {
    private final PostService postService;

    @Operation(summary = "Get post by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post was found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponse.class)
                    ) }),
            @ApiResponse(responseCode = "404", description = "Post was not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content),
    })
    @GetMapping("{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @Operation(summary = "Create new post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post was created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponse.class)
                    ) }),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content),
    })
    @PostMapping("new")
    public ResponseEntity<?> savePost(@RequestBody PostRequest pR) {
        return postService.saveCurrentUserPost(pR);}


    @Operation(summary = "Delete post by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post was found and deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Post was not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Not an owner or admin",
                    content = @Content),
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
       return postService.deleteCurrentUserPost(id);
    }



    @Operation(summary = "Update post by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post was found and updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponse.class)
                    ) }),
            @ApiResponse(responseCode = "404", description = "Post was not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Not an owner",
                    content = @Content),
    })
    @PutMapping("{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequest pR) {
        return postService.updateCurrentUserPost(id, pR);
    }
}
