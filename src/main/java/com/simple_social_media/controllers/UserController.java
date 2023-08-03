package com.simple_social_media.controllers;

import com.simple_social_media.entities.User;
import com.simple_social_media.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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

    //    @GetMapping("/users/{id}/posts")
//    public List<Post> getAllUserPosts(@PathVariable Long id) {
//        return userService.getAllUserPosts(id);
//    }
//
//    @PutMapping("")
//    public User updateUser(@RequestBody User user) {
//        userService.saveUser(user);
//        return user;
//    }




    //USERPROFILE



//
//    /////////////////////////////////////SUBSCRIPTIONS/SUBSCRIBERS/FRIENDS
//    @GetMapping("/users/{id}/subscriptions")
//    public List<User> getAllUserSubscriptions(@PathVariable Long id) {
//        return userService.getAllUserSubscriptions(id);
//    }
//    @GetMapping("/users/{id}/subscribers")
//    public List<User> getAllUserSubscribers(@PathVariable Long id) {
//        return userService.getAllUserSubscribes(id);
//    }
//    /////////////////////////////////////SUBSCRIPTIONS/SUBSCRIBERS/FRIENDS

}