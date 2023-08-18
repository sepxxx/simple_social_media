package com.SocialMediaApi.controllers;


import com.SocialMediaApi.dtos.responses.UserResponse;
import com.SocialMediaApi.services.FriendsAndSubsService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
@SecurityRequirement(name="bearerAuth")
public class FriendsAndSubsController {

    private final FriendsAndSubsService friendsAndSubsService;


    @Operation(summary = "Subscribe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription successful",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User was not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Can't follow yourself/Already following",
                    content = @Content)
    })
    @PostMapping("{targetUserId}/subscribers")
    public ResponseEntity<?> subscribeByUserId(@PathVariable Long targetUserId) {
            return friendsAndSubsService.subscribeByUserId(targetUserId);
    }


    @Operation(summary = "Unsubscribe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unsubscription successful",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User was not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Not following this id",
                    content = @Content)
    })
    @DeleteMapping("{targetUserId}/subscribers")
    public ResponseEntity<?> unsubscribeByUserId(@PathVariable Long targetUserId) {
            return friendsAndSubsService.unsubscribeByUserId(targetUserId);
    }


    @Operation(summary = "My friend requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of fr",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                    ) }),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content)
    })
    @GetMapping("me/friendRequests")
    public ResponseEntity<?> getUserFriendRequests() {
        return friendsAndSubsService.getUserFriendRequests();
    }


    @Operation(summary = "Get user subscribers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of subscribers",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                    ) }),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User was not found",
                    content = @Content),
    })
    @GetMapping("{targetUserId}/subscribers")
    public ResponseEntity<?> getUserSubscribersByUserId(@PathVariable Long targetUserId) {
        return friendsAndSubsService.getUserSubscribersByUserId(targetUserId);
    }


    @Operation(summary = "Get user subscriptions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of subscriptions",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                    ) }),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User was not found",
                    content = @Content),
    })
    @GetMapping("{targetUserId}/subscriptions")
    public ResponseEntity<?> getUserSubscriptionsByUserId(@PathVariable Long targetUserId) {
        return friendsAndSubsService.getUserSubscriptionsByUserId(targetUserId);
    }



    @Operation(summary = "Get user friends")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of friends",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                    ) }),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User was not found",
                    content = @Content),
    })
    @GetMapping("{targetUserId}/friends")
    public ResponseEntity<?> getUserFriendsByUserId(@PathVariable Long targetUserId) {
        return friendsAndSubsService.getUserFriendsByUserId(targetUserId);
    }


//    @GetMapping("me/subscriptions")
//    public ResponseEntity<?> getCurrentUserSubscriptions() {
//        return friendsAndSubsService.getCurrentUserSubscriptions();
//    }
//    @GetMapping("me/subscribers")
//    public ResponseEntity<?> getCurrentUserSubscribers() {
//        return friendsAndSubsService.getCurrentUserSubscribers();
//    }
//    @GetMapping("me/friends")
//    public ResponseEntity<?> getCurrentUserFriends() {
//        return friendsAndSubsService.getCurrentUserFriends();
//    }
//
//





}
