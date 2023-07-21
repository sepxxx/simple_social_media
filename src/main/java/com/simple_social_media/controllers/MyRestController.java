package com.simple_social_media.controllers;

import com.simple_social_media.entity.UserProfile;
import com.simple_social_media.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class MyRestController {
    @Autowired
    UserProfileService userProfileService;

    @GetMapping("/userProfiles")
    public List<UserProfile> showAllUserProfiles() {
        return userProfileService.getAllUserProfiles();
    }

    @PostMapping("/userProfiles")
    public UserProfile addUserProfile(@RequestBody UserProfile userProfile) {
        userProfileService.saveUserProfile(userProfile);
        return userProfile;
    }
}