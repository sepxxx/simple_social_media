package com.simple_social_media.controllers;

import com.simple_social_media.dtos.requests.MessageRequest;
import com.simple_social_media.services.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("conversations")
public class ConversationController {
    private final ConversationService conversationService;

    @GetMapping("")
    public ResponseEntity<?> getCurrentUserConversations() {return conversationService.getCurrentUserConversations();}

    @GetMapping("{id}/messages")
    public ResponseEntity<?> getCurrentUserConversationMessages(@PathVariable Long id) {return conversationService.getConversationMessagesById(id);}

    @PostMapping("messages/send")
    public ResponseEntity<?> sendMessageToUser(@RequestBody MessageRequest messageRequest) {
        return conversationService.sendMessageToUser(messageRequest);
    }
}
