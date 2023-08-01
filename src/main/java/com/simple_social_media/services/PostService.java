package com.simple_social_media.services;


import com.simple_social_media.repositories.PostRepository;
import com.simple_social_media.entities.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }


    public void savePost(Post post) {
        postRepository.save(post);
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
