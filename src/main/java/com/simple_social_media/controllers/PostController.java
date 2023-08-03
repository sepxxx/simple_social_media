package com.simple_social_media.controllers;

import com.simple_social_media.dtos.requests.PostRequest;
import com.simple_social_media.entities.Post;
import com.simple_social_media.services.PostService;
import com.simple_social_media.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController

@RequestMapping("posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;


    @GetMapping("{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @PostMapping("new")
    public ResponseEntity<?> savePost(@RequestBody PostRequest pR) {
        return postService.savePost(new Post(pR.getHeader(),pR.getText(), pR.getImage_url()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
       return postService.deletePost(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequest pR) {
        return postService.updatePost(id, pR);
    }
}
