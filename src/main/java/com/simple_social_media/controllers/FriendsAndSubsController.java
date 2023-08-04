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
    public ResponseEntity<?> sendFriendRequest(@PathVariable Long targetUserId) {
            return friendsAndSubsService.sendFriendRequest(targetUserId);
    }

    @GetMapping("mySubscriptions")
    public ResponseEntity<?> getCurrentUserSubscriptions() {
        return friendsAndSubsService.getCurrentUserSubscriptions();
    }

    @GetMapping("subscribers/{targetUserId}")
    public ResponseEntity<?> getUserSubscribersByUserId(@PathVariable Long targetUserId) {
        return friendsAndSubsService.getUserSubscribersByUserId(targetUserId);
    }


}
