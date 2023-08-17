package com.SocialMediaApi.repositories;

import com.SocialMediaApi.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
