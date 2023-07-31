package com.simple_social_media.dao;
import com.simple_social_media.entity.Post;
import com.simple_social_media.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {

}