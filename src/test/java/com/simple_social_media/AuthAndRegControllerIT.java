package com.simple_social_media;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple_social_media.dtos.requests.UserRegistrationRequest;
import com.simple_social_media.entities.User;
import com.simple_social_media.repositories.UserRepository;
import com.simple_social_media.services.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
//@ExtendWith(SpringExtension.class)
//@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AuthAndRegControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();;
    }


    @Test
    public void givenUser_whenCreateUser_thenReturnCreatedUserDto() throws Exception {
        // given - precondition or setup
        User user = new User("tttt", "tttt", "tttt");

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/reg")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.username",
                        is(user.getUsername())))
                .andExpect(jsonPath("$.email",
                        is(user.getEmail())));
    }

    @Test
    public void givenSameUsernames_whenCreateUser_thenReturnBadRequest() throws Exception {
        // given - precondition or setup
        User user1 = new User("tttt", "tttt", "tttt");
        User user2 = new User("tttt", "xxx", "xxx");
        userRepository.save(user1);

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/reg")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user2)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isBadRequest());
    }

    @Test
    public void givenSameUserEmails_whenCreateUser_thenReturnBadRequest() throws Exception {
        // given - precondition or setup
        User user1 = new User("xxx", "tttt", "tttt");
        User user2 = new User("tttt", "tttt", "xxx");
        userRepository.save(user1);

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/reg")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user2)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isBadRequest());
    }

    @Test
    public void givenValidUser_whenCreateAuthToken_thenReturnOk() throws Exception {
        // given - precondition or setup

        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        userService.saveUser(urr);
        JSONObject obj = new JSONObject();
        obj.put("username", urr.getUsername());
        obj.put("password", urr.getPassword());
        // when - action or behaviour that we are going test

        ResultActions response = mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.toString()));
        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isOk());
    }

    @Test
    public void givenInvalidUser_whenCreateAuthToken_thenReturnBadRequest() throws Exception {
        // given - precondition or setup
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        userService.saveUser(urr);
        JSONObject obj = new JSONObject();
        obj.put("username", "xxx");
        obj.put("password", "ttt");
        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.toString()));
        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isBadRequest());
    }


}
