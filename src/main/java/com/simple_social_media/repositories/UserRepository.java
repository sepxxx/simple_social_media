package com.simple_social_media.repositories;
import com.simple_social_media.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByName(String name);
}