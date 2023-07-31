package com.simple_social_media.controllers;

import com.simple_social_media.entity.Post;
import com.simple_social_media.entity.User;
import com.simple_social_media.services.PostService;
import com.simple_social_media.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;

    private final PostService postService;

    //USERPROFILE
    @GetMapping("/userProfiles")
    public List<User> showAllUserProfiles() {
        return userService.getAllUserProfiles();
    }

    @GetMapping("/userProfiles/{id}")
    public User getUserProfile(@PathVariable Long id) {
        return userService.getUserProfile(id);
    }

    @PostMapping("/userProfiles")
    public User addUserProfile(@RequestBody User user) {
        userService.saveUserProfile(user);
        return user;
    }

    @PutMapping("/userProfiles")
    public User updateUserProfile(@RequestBody User user) {
        userService.saveUserProfile(user);
        return user;
    }

    @DeleteMapping("/userProfiles/{id}")
    public String deleteUserProfile(@PathVariable Long id) {
        userService.deleteUserProfile(id);
        return "deleted userProfile id: "+id+" from db";
    }


    //USERPROFILE


    /////////////////////////////////////POSTs
    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable int id) {
        return postService.getPost(id);
    }
    @GetMapping("/userProfiles/{id}/posts")
    public List<Post> getAllUserProfilePosts(@PathVariable Long id) {
        return userService.getAllUserProfilePosts(id);
    }
    @PostMapping("/userProfiles/{id}/posts")
    public Post addPost(@RequestBody Post post, @PathVariable Long id) {
        //получаем пользователя, если есть
        //добавляем в его список постов пост
        //сохраняем пост в табличку
        //если нет, то ничего не делаем
        User user = userService.getUserProfile(id);
        if(user ==null) return null;
        user.addPostToUser(post);
        //нужно скорее всего будет обновить юзера в табличке чтобы запись была в jointable
        postService.savePost(post);
        return post;
    }

    @DeleteMapping("/posts/{id}")
    public String deletePost(@PathVariable int id) {
        //тут нужно скорее всего проверить кто отправляет запрос
        postService.deletePost(id);
        return "deleted user post with id: "+id+" from db";
    }
    /////////////////////////////////////POSTs

    /////////////////////////////////////SUBSCRIPTIONS/SUBSCRIBERS/FRIENDS
    @GetMapping("/userProfiles/{id}/subscriptions")
    public List<User> getAllUserProfileSubscriptions(@PathVariable Long id) {
        return userService.getAllUserProfileSubscriptions(id);
    }
    @GetMapping("/userProfiles/{id}/subscribers")
    public List<User> getAllUserProfileSubscribers(@PathVariable Long id) {
        return userService.getAllUserProfileSubscribes(id);
    }
    /////////////////////////////////////SUBSCRIPTIONS/SUBSCRIBERS/FRIENDS

}