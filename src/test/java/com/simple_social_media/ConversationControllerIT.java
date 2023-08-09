package com.simple_social_media;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple_social_media.dtos.requests.UserRegistrationRequest;
import com.simple_social_media.entities.Conversation;
import com.simple_social_media.entities.Message;
import com.simple_social_media.entities.User;
import com.simple_social_media.repositories.UserRepository;
import com.simple_social_media.services.ConversationService;
import com.simple_social_media.services.UserService;
import com.simple_social_media.utils.AuthMethodForTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

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
        userRepository.deleteAll();;
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

//    @Test
//    @DisplayName("GET /conversations by user with convs returns List Of ConversationResponseDto")
//    public void givenContextUserWithConversations_whenGetConversations_thenReturnsListOfConversationResponseDto() throws Exception {
//        //given
//        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
//        User user = userService.saveUser(urr);
////        UserRegistrationRequest urr2 = new UserRegistrationRequest("xxx", "xxx", "xxx");
////        User user2 = userService.saveUser(urr2);
////
//
//
//
//        user.getConversations(List.of(new Conversation(
//                "1to2"
//                ).setMessageList(
//                        new Message()
//                ))
//        );
//        //when
//        ResultActions resultActions = mockMvc.perform(
//                get("/conversations")
//                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
//        //then
//        resultActions.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()", is(0)));
//    }


}
