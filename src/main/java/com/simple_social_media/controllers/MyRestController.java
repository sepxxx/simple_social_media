package com.simple_social_media.controllers;

import com.simple_social_media.entity.Post;
import com.simple_social_media.entity.UserProfile;
import com.simple_social_media.services.PostService;
import com.simple_social_media.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class MyRestController {
    @Autowired
    UserProfileService userProfileService;

    @Autowired
    PostService postService;

    //USERPROFILE
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

    @GetMapping("/userProfiles/{id}/posts")
    public List<Post> getAllUserProfilePosts(@PathVariable int id) {
        return userProfileService.getAllUserProfilePosts(id);
    }
    //USERPROFILE


    //POST
    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable int id) {
        return postService.getPost(id);
    }

    @PostMapping("/userProfiles/{id}/posts")
    public Post addPost(@RequestBody Post post, @PathVariable int id) {
        //получаем пользователя, если есть
        //добавляем в его список постов пост
        //сохраняем пост в табличку
        //если нет, то ничего не делаем
        UserProfile userProfile = userProfileService.getUserProfile(id);
        if(userProfile==null) return null;
        userProfile.addPostToUser(post);
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

}