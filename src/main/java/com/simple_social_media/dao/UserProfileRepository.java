package com.simple_social_media.dao;
import com.simple_social_media.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserProfileRepository extends JpaRepository<User,Long> {

}