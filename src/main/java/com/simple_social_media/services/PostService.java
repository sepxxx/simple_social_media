package com.simple_social_media.services;

import com.simple_social_media.entity.Post;

import java.util.List;

public interface PostService {

    List<Post> getAllPosts();
    void savePost(Post post);
    Post getPost(int id);
    void deletePost(int id);
}
