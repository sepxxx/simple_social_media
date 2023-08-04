package com.simple_social_media.controllers;


import com.simple_social_media.services.FriendsAndSubsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class FriendsAndSubsController {

    private final FriendsAndSubsService friendsAndSubsService;

    @PostMapping("{targetUserId}/subscribers")
    public ResponseEntity<?> subscribeByUserId(@PathVariable Long targetUserId) {
            return friendsAndSubsService.subscribeByUserId(targetUserId);
    }
    @DeleteMapping("{targetUserId}/subscribers")
    public ResponseEntity<?> unsubscribeByUserId(@PathVariable Long targetUserId) {
            return friendsAndSubsService.unsubscribeByUserId(targetUserId);
    }

    @GetMapping("me/subscriptions")
    public ResponseEntity<?> getCurrentUserSubscriptions() {
        return friendsAndSubsService.getCurrentUserSubscriptions();
    }
    @GetMapping("me/friendRequests")
    public ResponseEntity<?> getCurrentUserActiveFriendRequests() {
        return friendsAndSubsService.getCurrentUserActiveFriendRequests();
    }

    @GetMapping("me/subscribers")
    public ResponseEntity<?> getCurrentUserSubscribers() {
        return friendsAndSubsService.getCurrentUserSubscribers();
    }
    @GetMapping("me/friends")
    public ResponseEntity<?> getCurrentUserFriends() {
        return friendsAndSubsService.getCurrentUserFriends();
    }


    @GetMapping("{targetUserId}/subscribers")
    public ResponseEntity<?> getUserSubscribersByUserId(@PathVariable Long targetUserId) {
        return friendsAndSubsService.getUserSubscribersByUserId(targetUserId);
    }








}
