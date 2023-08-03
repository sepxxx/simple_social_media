package com.simple_social_media.controllers;

import com.simple_social_media.entities.Post;
import com.simple_social_media.entities.User;
import com.simple_social_media.services.PostService;
import com.simple_social_media.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;



    //USERPROFILE
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    //    @GetMapping("/users/{id}/posts")
//    public List<Post> getAllUserPosts(@PathVariable Long id) {
//        return userService.getAllUserPosts(id);
//    }
//

//    @PostMapping("/users")
//    public User saveUser(@RequestBody User user) {
//        userService.saveUser(user);
//        return user;
//    }
//
//    @PutMapping("/users")
//    public User updateUser(@RequestBody User user) {
//        userService.saveUser(user);
//        return user;
//    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "deleted userProfile id: "+id+" from db";
    }


    //USERPROFILE




    /////////////////////////////////////SUBSCRIPTIONS/SUBSCRIBERS/FRIENDS
    @GetMapping("/users/{id}/subscriptions")
    public List<User> getAllUserSubscriptions(@PathVariable Long id) {
        return userService.getAllUserSubscriptions(id);
    }
    @GetMapping("/users/{id}/subscribers")
    public List<User> getAllUserSubscribers(@PathVariable Long id) {
        return userService.getAllUserSubscribes(id);
    }
    /////////////////////////////////////SUBSCRIPTIONS/SUBSCRIBERS/FRIENDS

}