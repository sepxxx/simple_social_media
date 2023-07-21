package com.simple_social_media.dao;
import com.simple_social_media.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {
}