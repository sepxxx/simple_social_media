package com.simple_social_media.services;


import com.simple_social_media.entities.User;
import com.simple_social_media.repositories.PostRepository;
import com.simple_social_media.entities.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }


    public Post savePost(Post post) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(null != securityContext.getAuthentication()){
            String contextUserName = (String) securityContext.getAuthentication().getPrincipal();
            User dbUser = userService.findByUsername(contextUserName).get();
//            dbUser.addPostToUser(post);
            post.setUser(dbUser);
            return postRepository.save(post);
        }
        return null;
    }


    public Post getPost(Long id) {
        Optional<Post> optional = postRepository.findById(id);
        Post post = null;
        if(optional.isPresent())
            post = optional.get();
        return post;
    }


    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
