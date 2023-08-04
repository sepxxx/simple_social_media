package com.simple_social_media.controllers;


import com.simple_social_media.services.FriendsAndSubsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FriendsAndSubsController {

    private final FriendsAndSubsService friendsAndSubsService;

    @PostMapping("subscribe/{targetUserId}")
    public ResponseEntity<?> sendFriendRequestByUserId(@PathVariable Long targetUserId) {
            return friendsAndSubsService.sendFriendRequestByUserId(targetUserId);
    }
    @PostMapping("unsubscribe/{targetUserId}")
    public ResponseEntity<?> unsubscribeByUserId(@PathVariable Long targetUserId) {
            return friendsAndSubsService.unsubscribeByUserId(targetUserId);
    }

    @GetMapping("mySubscriptions")
    public ResponseEntity<?> getCurrentUserSubscriptions() {
        return friendsAndSubsService.getCurrentUserSubscriptions();
    }
    @GetMapping("myFriendRequests")
    public ResponseEntity<?> getCurrentUserActiveFriendRequests() {
        return friendsAndSubsService.getCurrentUserActiveFriendRequests();
    }

    @GetMapping("mySubscribers")
    public ResponseEntity<?> getCurrentUserSubscribers() {
        return friendsAndSubsService.getCurrentUserSubscribers();
    }

    @GetMapping("subscribers/{targetUserId}")
    public ResponseEntity<?> getUserSubscribersByUserId(@PathVariable Long targetUserId) {
        return friendsAndSubsService.getUserSubscribersByUserId(targetUserId);
    }








}
