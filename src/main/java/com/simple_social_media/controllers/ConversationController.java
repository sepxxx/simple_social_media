package com.simple_social_media.controllers;

import com.simple_social_media.services.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("conversations")
public class ConversationController {
    private final ConversationService conversationService;

    @PostMapping("with/{targetUserId}")
    public ResponseEntity<?> createConversation(@PathVariable Long targetUserId) {return conversationService.createConversation(targetUserId);}

    @GetMapping("")
    public ResponseEntity<?> getCurrentUserConversations() {return conversationService.getCurrentUserConversations();}
}
