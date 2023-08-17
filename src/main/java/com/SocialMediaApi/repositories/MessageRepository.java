package com.SocialMediaApi.repositories;

import com.SocialMediaApi.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByConversationId(Long conversationId);
}
