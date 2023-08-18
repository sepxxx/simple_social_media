package com.SocialMediaApi;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.SocialMediaApi.dtos.requests.UserRegistrationRequest;
import com.SocialMediaApi.entities.Conversation;
import com.SocialMediaApi.entities.Message;
import com.SocialMediaApi.entities.User;
import com.SocialMediaApi.repositories.UserRepository;
import com.SocialMediaApi.services.ConversationService;
import com.SocialMediaApi.services.UserService;
import com.SocialMediaApi.utils.AuthMethodForTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class ConversationControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ConversationService conversationService;

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /conversations by user with no convs returns List Of ConversationResponseDto")
    public void givenContextUserWithNoConversations_whenGetConversations_thenReturnsListOfConversationResponseDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        userService.saveUser(urr);
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/conversations")
                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    @DisplayName("GET /conversations by user with 1 conv(no messages) returns List Of ConversationResponseDto size 1")
    public void givenContextUserWith1Conversation_whenGetConversations_thenReturnsListOfConversationResponseDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user = userService.saveUser(urr);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        //нужно получить id из базы
        Conversation conversation = conversationService.saveConversationByEntity(new Conversation("1:2"));
        conversation.addUserToConversation(user);
        conversation.addUserToConversation(user2);
        conversationService.saveConversationByEntity(conversation);
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/conversations")
                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    @DisplayName("GET /conversations by user with 2 conv(no messages) returns List Of ConversationResponseDto size 2")
    public void givenContextUserWith2Conversations_whenGetConversations_thenReturnsListOfConversationResponseDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user = userService.saveUser(urr);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        UserRegistrationRequest urr3 = new UserRegistrationRequest("bbb", "bbb", "bbb");
        User user3 = userService.saveUser(urr3);
        //нужно получить id из базы
        Conversation conversation = conversationService.saveConversationByEntity(new Conversation("1:2"));
        conversation.addUserToConversation(user);
        conversation.addUserToConversation(user2);
        conversationService.saveConversationByEntity(conversation);

        Conversation conversation2 = conversationService.saveConversationByEntity(new Conversation("1:3"));
        conversation2.addUserToConversation(user);
        conversation2.addUserToConversation(user3);
        conversationService.saveConversationByEntity(conversation2);


        //when
        ResultActions resultActions = mockMvc.perform(
                get("/conversations")
                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }



    @Test
    @DisplayName("GET /conversations by user with 2 conv(1 with messages) returns List Of ConversationResponseDto size 2")
    public void givenContextUserWith2Conversations1WithMessages_whenGetConversations_thenReturnsListOfConversationResponseDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user = userService.saveUser(urr);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        UserRegistrationRequest urr3 = new UserRegistrationRequest("bbb", "bbb", "bbb");
        User user3 = userService.saveUser(urr3);
        //нужно получить id из базы
        Conversation conversation = conversationService.saveConversationByEntity(new Conversation("1:2"));
        conversation.addUserToConversation(user);
        conversation.addUserToConversation(user2);
        conversation.addMessageToConversation(new Message(conversation.getId(), user.getId(), "sup", new Date()));
        conversation.addMessageToConversation(new Message(conversation.getId(), user2.getId(), "hello", new Date()));
        conversationService.saveConversationByEntity(conversation);


        Conversation conversation2 = conversationService.saveConversationByEntity(new Conversation("1:3"));
        conversation2.addUserToConversation(user);
        conversation2.addUserToConversation(user3);
        conversationService.saveConversationByEntity(conversation2);


        //when
        ResultActions resultActions = mockMvc.perform(
                get("/conversations")
                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }
}
