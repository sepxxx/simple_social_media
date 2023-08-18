package com.SocialMediaApi.controllers;

import com.SocialMediaApi.dtos.requests.MessageRequest;
import com.SocialMediaApi.dtos.responses.ConversationResponse;
import com.SocialMediaApi.dtos.responses.MessageResponse;
import com.SocialMediaApi.dtos.responses.UserResponse;
import com.SocialMediaApi.services.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name="bearerAuth")
@RequestMapping("conversations")
public class ConversationController {
    private final ConversationService conversationService;


    @Operation(summary = "get current user conversations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of conversations",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ConversationResponse.class))
                    ) }),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content)
    })
    @GetMapping("")
    public ResponseEntity<?> getCurrentUserConversations() {return conversationService.getCurrentUserConversations();}


    @Operation(summary = "get conversation messages by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of messages",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MessageResponse.class))
                    ) }),
            @ApiResponse(responseCode = "404", description = "Conversation was not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Not a member of this conversation",
                    content = @Content)
    })
    @GetMapping("{id}/messages")
    public ResponseEntity<?> getCurrentUserConversationMessages(@PathVariable Long id) {return conversationService.getConversationMessagesById(id);}




    @Operation(summary = "send message by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message was sent",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Target user was not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Not a friend of target user",
                    content = @Content)
    })
    @PostMapping("messages/send")
    public ResponseEntity<?> sendMessageToUser(@RequestBody MessageRequest messageRequest) {
        return conversationService.sendMessageToUser(messageRequest);
    }
}
