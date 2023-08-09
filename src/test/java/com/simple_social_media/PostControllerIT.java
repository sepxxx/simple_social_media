package com.simple_social_media;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple_social_media.dtos.requests.PostRequest;
import com.simple_social_media.dtos.requests.UserRegistrationRequest;
import com.simple_social_media.dtos.responses.PostResponse;
import com.simple_social_media.entities.Post;
import com.simple_social_media.entities.User;
import com.simple_social_media.repositories.UserRepository;
import com.simple_social_media.services.PostService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostService postService;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();;
    }

    @Test
    @DisplayName("GET posts/id with invalid id returns 404")
    public void givenInvalidId_whenGetPost_thenReturns404() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx","xxx","xxx");
        userService.saveUser(urr);
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/posts/{id}", 777)
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET posts/id with valid id returns post dto")
    public void givenValidId_whenGetPost_thenReturnsPostDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx","xxx","xxx");
        User user = userService.saveUser(urr);
        user.setPosts(List.of(new Post("header", "text", "url")));
        user = userService.saveUserByEntity(user);
        Long postId = user.getPosts().get(0).getId();
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/posts/{id}", postId)
                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }
}
