package com.simple_social_media.repositories;

import com.simple_social_media.entities.Conversation;
import com.simple_social_media.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("SELECT c FROM Conversation c WHERE :user1 MEMBER OF c.usersList AND :user2 MEMBER OF c.usersList")
    Optional<Conversation> findByUsers(User user1, User user2);
    @Override
    Optional<Conversation> findById(Long id);
}
