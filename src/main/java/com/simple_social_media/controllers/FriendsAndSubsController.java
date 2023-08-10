package com.simple_social_media.controllers;


import com.simple_social_media.dtos.responses.PostResponse;
import com.simple_social_media.services.FriendsAndSubsService;
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
            @ApiResponse(responseCode = "400", description = "Can't follow yourself",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Already following",
                    content = @Content)
    })
    @PostMapping("{targetUserId}/subscribers")
    public ResponseEntity<?> subscribeByUserId(@PathVariable Long targetUserId) {
            return friendsAndSubsService.subscribeByUserId(targetUserId);
    }
    @DeleteMapping("{targetUserId}/subscribers")
    public ResponseEntity<?> unsubscribeByUserId(@PathVariable Long targetUserId) {
            return friendsAndSubsService.unsubscribeByUserId(targetUserId);
    }

    @GetMapping("me/friendRequests")
    public ResponseEntity<?> getUserFriendRequests() {
        return friendsAndSubsService.getUserFriendRequests();
    }

    @GetMapping("{targetUserId}/subscribers")
    public ResponseEntity<?> getUserSubscribersByUserId(@PathVariable Long targetUserId) {
        return friendsAndSubsService.getUserSubscribersByUserId(targetUserId);
    }
    @GetMapping("{targetUserId}/subscriptions")
    public ResponseEntity<?> getUserSubscriptionsByUserId(@PathVariable Long targetUserId) {
        return friendsAndSubsService.getUserSubscriptionsByUserId(targetUserId);
    }
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
