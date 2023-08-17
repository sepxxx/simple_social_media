package com.SocialMediaApi.repositories;
import com.SocialMediaApi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String name);
    Optional<User> findById(Long id);
    Boolean existsByUsername(String name);
    Boolean existsByEmail(String mail);


}