package com.simple_social_media.services;


import com.simple_social_media.dao.PostRepository;
import com.simple_social_media.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public void savePost(Post post) {
        postRepository.save(post);
    }

    @Override
    public Post getPost(int id) {
        Optional<Post> optional = postRepository.findById(id);
        Post post = null;
        if(optional.isPresent())
            post = optional.get();
        return post;
    }

    @Override
    public void deletePost(int id) {
        postRepository.deleteById(id);
    }
}
