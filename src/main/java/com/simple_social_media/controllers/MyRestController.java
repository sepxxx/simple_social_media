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

    @GetMapping("/userProfiles/{id}")
    public UserProfile getUserProfile(@PathVariable int id) {
        return userProfileService.getUserProfile(id);
    }

    @PostMapping("/userProfiles")
    public UserProfile addUserProfile(@RequestBody UserProfile userProfile) {
        userProfileService.saveUserProfile(userProfile);
        return userProfile;
    }

    @PutMapping("/userProfiles")
    public UserProfile updateUserProfile(@RequestBody UserProfile userProfile) {
        userProfileService.saveUserProfile(userProfile);
        return userProfile;
    }

    @DeleteMapping("/userProfiles/{id}")
    public String deleteUserProfile(@PathVariable int id) {
        userProfileService.deleteUserProfile(id);
        return "deleted userProfile id: "+id+" from db";
    }

}