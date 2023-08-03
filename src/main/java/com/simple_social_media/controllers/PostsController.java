package com.simple_social_media.controllers;

import com.simple_social_media.dtos.requests.PostRequest;
import com.simple_social_media.entities.Post;
import com.simple_social_media.entities.User;
import com.simple_social_media.security.CustomUser;
import com.simple_social_media.services.PostService;
import com.simple_social_media.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
//@RequestMapping("")
@RequiredArgsConstructor
public class PostsController {
    private final PostService postService;

    private final UserService userService;

    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

//    @GetMapping("/users/{id}/posts")
//    public List<Post> getAllUserPosts(@PathVariable Long id) {
//        return userService.getAllUserPosts(id);
//    }
//
    @PostMapping("/posts/new")
    public ResponseEntity<?> savePost(@RequestBody PostRequest pR) {
        return postService.savePost(new Post(pR.getHeader(),pR.getText(), pR.getImage_url()));
    }

//    @PostMapping("/users/{id}/posts")
//    public Post savePost(@RequestBody Post post, @PathVariable Long id) {
//        //получаем пользователя, если есть
//        //добавляем в его список постов пост
//        //сохраняем пост в табличку
//        //если нет, то ничего не делаем
//        User user = userService.getUser(id);
//        if(user ==null) return null;
//        user.addPostToUser(post);
//
//        postService.savePost(post);
//        return post;
//    }

    @DeleteMapping("{id}")
    public String deletePost(@PathVariable Long id) {
        //тут нужно скорее всего проверить кто отправляет запрос
        postService.deletePost(id);
        return "deleted user post with id: "+id+" from db";
    }

}
