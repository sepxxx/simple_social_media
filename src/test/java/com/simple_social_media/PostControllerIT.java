package com.simple_social_media;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple_social_media.dtos.requests.PostRequest;
import com.simple_social_media.dtos.requests.UserRegistrationRequest;
import com.simple_social_media.dtos.responses.PostResponse;
import com.simple_social_media.entities.Post;
import com.simple_social_media.entities.User;
import com.simple_social_media.repositories.UserRepository;
import com.simple_social_media.services.PostService;
import com.simple_social_media.services.RoleService;
import com.simple_social_media.services.UserService;
import com.simple_social_media.utils.AuthMethodForTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
    RoleService roleService;
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
                get("/posts/{id}", 1234567890)
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET posts/id with valid id returns postResponseDto")
    public void givenValidId_whenGetPost_thenReturnsPostResponseDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx","xxx","xxx");
        User user = userService.saveUser(urr);
        Post post = new Post("header", "text", "url");
        user.setPosts(List.of(post));
        user = userService.saveUserByEntity(user);
        Long postId = user.getPosts().get(0).getId();
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/posts/{id}", postId)
                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header", is(post.getHeader())))
                .andExpect(jsonPath("$.id", is(postId), Long.class))
                .andExpect(jsonPath("$.text", is(post.getText())))
                .andExpect(jsonPath("$.imageUrl", is(post.getImage_url())))
                .andExpect(jsonPath("$.usernameCreatedBy", is(user.getUsername())));
    }

    @Test
    @DisplayName("POST posts/new with postRequestDto returns postResponseDto")
    public void givenPostRequestDto_whenSavePost_thenReturnsPostResponseDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx","xxx","xxx");
        User user = userService.saveUser(urr);

        PostRequest postRequest = new PostRequest("header", "text", "url");
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/posts/new")
                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))
        );
        //then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header", is(postRequest.getHeader())))
                .andExpect(jsonPath("$.text", is(postRequest.getText())))
                .andExpect(jsonPath("$.imageUrl", is(postRequest.getImage_url())))
                .andExpect(jsonPath("$.usernameCreatedBy", is(user.getUsername())));
    }


    @Test
    @DisplayName("DELETE posts/id with valid id by creator of post returns 200")
    public void givenCreatorOfPostWithValidPostId_whenDeletePost_thenReturns200() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx","xxx","xxx");
        User user = userService.saveUser(urr);

        Post post = new Post("header", "text", "url");
        user.setPosts(List.of(post));
        user = userService.saveUserByEntity(user);
        Long postId = user.getPosts().get(0).getId();

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/posts/{id}", postId)
                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
        );
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE posts/id with invalid id returns 404")
    public void givenCreatorOfPostWithInvalidPostId_whenDeletePost_thenReturns404() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx","xxx","xxx");
        User user = userService.saveUser(urr);

//        Post post = new Post("header", "text", "url");
//        user.setPosts(List.of(post));
//        user = userService.saveUserByEntity(user);
//        Long postId = user.getPosts().get(0).getId();

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/posts/{id}",1234567890)
                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
        );
        //then
        resultActions.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE posts/id with valid id NOT by creator of post AND NOT ADMIN returns 403")
    public void givenNotCreatorOfPostNotAdminWithValidPostId_whenDeletePost_thenReturns403() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx","xxx","xxx");
        User user = userService.saveUser(urr);

        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy","yyy","yyy");
        User user2 = userService.saveUser(urr2);

        Post post = new Post("header", "text", "url");
        user.setPosts(List.of(post));
        user = userService.saveUserByEntity(user);
        Long postId = user.getPosts().get(0).getId();

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/posts/{id}",postId)
                        .header("Authorization", AuthMethodForTests.getToken(urr2, mockMvc, objectMapper))
        );
        //then
        resultActions.andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE posts/id with valid id BY ADMIN returns 200")
    public void givenAdminWithValidPostId_whenDeletePost_thenReturns200() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx","xxx","xxx");
        User user = userService.saveUser(urr);
        Post post = new Post("header", "text", "url");
        user.setPosts(List.of(post));
        user = userService.saveUserByEntity(user);
        Long postId = user.getPosts().get(0).getId();


        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy","yyy","yyy");
        User user2 = userService.saveUser(urr2);
        user2.setRoles(List.of(roleService.getAdminRole(), roleService.getUserRole()));
        userService.saveUserByEntity(user2);

        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/posts/{id}",postId)
                        .header("Authorization", AuthMethodForTests.getToken(urr2, mockMvc, objectMapper))
        );
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT posts/id with valid id BY creator returns postRequestDto")
    public void givenCreatorOfPostWithValidPostId_whenUpdatePost_thenReturnsPostRequestDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx","xxx","xxx");
        User user = userService.saveUser(urr);
        Post post = new Post("header", "text", "url");
        user.setPosts(List.of(post));
        user = userService.saveUserByEntity(user);
        Long postId = user.getPosts().get(0).getId();

        PostRequest postRequest = new PostRequest("newheader", "newtext", "newurl");

        //when
        ResultActions resultActions = mockMvc.perform(
                put("/posts/{id}", postId)
                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest))
        );
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header", is(postRequest.getHeader())))
                .andExpect(jsonPath("$.text", is(postRequest.getText())))
                .andExpect(jsonPath("$.imageUrl", is(postRequest.getImage_url())))
                .andExpect(jsonPath("$.usernameCreatedBy", is(user.getUsername())));
    }


    @Test
    @DisplayName("PUT posts/id with valid id BY NOT creator returns 403")
    public void givenNotCreatorOfPostWithValidPostId_whenUpdatePost_thenReturns403() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx","xxx","xxx");
        User user = userService.saveUser(urr);
        Post post = new Post("header", "text", "url");
        user.setPosts(List.of(post));
        user = userService.saveUserByEntity(user);
        Long postId = user.getPosts().get(0).getId();
        PostRequest postRequest = new PostRequest("newheader", "newtext", "newurl");

        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy","yyy","yyy");
        userService.saveUser(urr2);

        //when
        ResultActions resultActions = mockMvc.perform(
                put("/posts/{id}", postId)
                        .header("Authorization", AuthMethodForTests.getToken(urr2, mockMvc, objectMapper))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest))
        );
        //then
        resultActions.andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("PUT posts/id with invalid id returns 404")
    public void givenInvalidPostId_whenUpdatePost_thenReturns404() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx","xxx","xxx");
        User user = userService.saveUser(urr);
        PostRequest postRequest = new PostRequest("newheader", "newtext", "newurl");
        //when
        ResultActions resultActions = mockMvc.perform(
                put("/posts/{id}", 1234567890)
                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest))
        );
        //then
        resultActions.andDo(print())
                .andExpect(status().isNotFound());

    }


}
