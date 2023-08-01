package com.simple_social_media.controllers;

import com.simple_social_media.entities.Post;
import com.simple_social_media.entities.User;
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
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

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


    /////////////////////////////////////POSTs
    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }
    @GetMapping("/users/{id}/posts")
    public List<Post> getAllUserPosts(@PathVariable Long id) {
        return userService.getAllUserPosts(id);
    }
    @PostMapping("/users/{id}/posts")
    public Post savePost(@RequestBody Post post, @PathVariable Long id) {
        //получаем пользователя, если есть
        //добавляем в его список постов пост
        //сохраняем пост в табличку
        //если нет, то ничего не делаем
        User user = userService.getUser(id);
        if(user ==null) return null;
        user.addPostToUser(post);
        //нужно скорее всего будет обновить юзера в табличке чтобы запись была в jointable
        postService.savePost(post);
        return post;
    }

    @DeleteMapping("/posts/{id}")
    public String deletePost(@PathVariable Long id) {
        //тут нужно скорее всего проверить кто отправляет запрос
        postService.deletePost(id);
        return "deleted user post with id: "+id+" from db";
    }
    /////////////////////////////////////POSTs

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