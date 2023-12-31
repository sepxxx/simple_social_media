package com.SocialMediaApi;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.SocialMediaApi.dtos.requests.ActivityFeedRequest;
import com.SocialMediaApi.dtos.requests.UserRegistrationRequest;
import com.SocialMediaApi.entities.Post;
import com.SocialMediaApi.entities.User;
import com.SocialMediaApi.repositories.PostRepository;
import com.SocialMediaApi.repositories.UserRepository;
import com.SocialMediaApi.services.PostService;
import com.SocialMediaApi.services.UserService;
import com.SocialMediaApi.utils.AuthMethodForTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class ActivityFeedController {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;


    @BeforeEach
    void setup() {
        userRepository.deleteAll();;
    }


    @Test
    @DisplayName("GET /feed by user with subscriptions for users with posts WHEN Nof posts = limit page 1")
    public void givenUserWithSubscriptionForUsersWithPosts_whenGetActivityFeed_thenReturnsActivityFeedResponse() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);

        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        Post post = postRepository.save(new Post("header1", "text1", "null"));
        user2.addPostToUser(post);

        UserRegistrationRequest urr3 = new UserRegistrationRequest("zzz", "zzz", "zzz");
        User user3 = userService.saveUser(urr3);
        Post post2 = postRepository.save(new Post("header2", "text2", "null"));
        user3.addPostToUser(post2);

        user1.addSubscriptionToUser(user2);
        user1.addSubscriptionToUser(user3);
        userService.saveUserByEntity(user1);


        //when
        ResultActions resultActions = mockMvc.perform(get("/feed")
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ActivityFeedRequest(2,1))));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagesAmount", is(1)))
                .andExpect(jsonPath("$.posts.size()", is(2)));
    }

    @Test
    @DisplayName("GET /feed by user with subscriptions for users with posts WHEN Nof posts < limit page 1")
    public void givenUserWithSubscriptionForUsersWithPosts_whenGetActivityFeed_thenReturnsActivityFeedResponse_2() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);

        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        Post post = postRepository.save(new Post("header1", "text1", "null"));
        user2.addPostToUser(post);

        UserRegistrationRequest urr3 = new UserRegistrationRequest("zzz", "zzz", "zzz");
        User user3 = userService.saveUser(urr3);
        Post post2 = postRepository.save(new Post("header2", "text2", "null"));
        user3.addPostToUser(post2);

        user1.addSubscriptionToUser(user2);
        user1.addSubscriptionToUser(user3);
        userService.saveUserByEntity(user1);


        //when
        ResultActions resultActions = mockMvc.perform(get("/feed")
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ActivityFeedRequest(5,1))));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagesAmount", is(1)))
                .andExpect(jsonPath("$.posts.size()", is(2)));
    }

    @Test
    @DisplayName("GET /feed by user with subscriptions for users with posts WHEN Nof posts < limit page 2")
    public void givenUserWithSubscriptionForUsersWithPosts_whenGetActivityFeed_thenReturns404_3() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);

        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        Post post = postRepository.save(new Post("header1", "text1", "null"));
        user2.addPostToUser(post);

        UserRegistrationRequest urr3 = new UserRegistrationRequest("zzz", "zzz", "zzz");
        User user3 = userService.saveUser(urr3);
        Post post2 = postRepository.save(new Post("header2", "text2", "null"));
        user3.addPostToUser(post2);

        user1.addSubscriptionToUser(user2);
        user1.addSubscriptionToUser(user3);
        userService.saveUserByEntity(user1);


        //when
        ResultActions resultActions = mockMvc.perform(get("/feed")
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ActivityFeedRequest(5,2))));
        //then
        resultActions.andDo(print())
                .andExpect(status().isNotFound());
    }



    @Test
    @DisplayName("GET /feed by user with subscriptions for users with posts WHEN Nof posts > limit page 1")
    public void givenUserWithSubscriptionForUsersWithPosts_whenGetActivityFeed_thenReturnsActivityFeedResponse_4() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);

        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        Post post = postRepository.save(new Post("header1", "text1", "null"));
        user2.addPostToUser(post);

        UserRegistrationRequest urr3 = new UserRegistrationRequest("zzz", "zzz", "zzz");
        User user3 = userService.saveUser(urr3);
        Post post2 = postRepository.save(new Post("header2", "text2", "null"));
        user3.addPostToUser(post2);

        UserRegistrationRequest urr4 = new UserRegistrationRequest("aaa", "aaa", "aaa");
        User user4 = userService.saveUser(urr4);
        Post post3 = postRepository.save(new Post("header3", "text3", "null"));
        user4.addPostToUser(post3);


        user1.addSubscriptionToUser(user2);
        user1.addSubscriptionToUser(user3);
        user1.addSubscriptionToUser(user4);
        userService.saveUserByEntity(user1);


        //when
        ResultActions resultActions = mockMvc.perform(get("/feed")
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ActivityFeedRequest(2,1))));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagesAmount", is(2)))
                .andExpect(jsonPath("$.posts.size()", is(2)));
    }


    @Test
    @DisplayName("GET /feed by user with subscriptions for users with posts WHEN Nof posts > limit page 2")
    public void givenUserWithSubscriptionForUsersWithPosts_whenGetActivityFeed_thenReturnsActivityFeedResponse_5() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);

        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        Post post = postRepository.save(new Post("header1", "text1", "null"));
        user2.addPostToUser(post);

        UserRegistrationRequest urr3 = new UserRegistrationRequest("zzz", "zzz", "zzz");
        User user3 = userService.saveUser(urr3);
        Post post2 = postRepository.save(new Post("header2", "text2", "null"));
        user3.addPostToUser(post2);

        UserRegistrationRequest urr4 = new UserRegistrationRequest("aaa", "aaa", "aaa");
        User user4 = userService.saveUser(urr4);
        Post post3 = postRepository.save(new Post("header3", "text3", "null"));
        user4.addPostToUser(post3);


        user1.addSubscriptionToUser(user2);
        user1.addSubscriptionToUser(user3);
        user1.addSubscriptionToUser(user4);
        userService.saveUserByEntity(user1);


        //when
        ResultActions resultActions = mockMvc.perform(get("/feed")
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ActivityFeedRequest(2,2))));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagesAmount", is(2)))
                .andExpect(jsonPath("$.posts.size()", is(1)));
    }



    @Test
    public void givenUserWithSubscriptionForUsersWithPosts_whenGetActivityFeed_thenReturnsActivityFeedResponse_6() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);

        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        Post post = postRepository.save(new Post("header1", "text1", "null"));
        user2.addPostToUser(post);

        UserRegistrationRequest urr3 = new UserRegistrationRequest("zzz", "zzz", "zzz");
        User user3 = userService.saveUser(urr3);
        Post post2 = postRepository.save(new Post("header2", "text2", "null"));
        user3.addPostToUser(post2);

        UserRegistrationRequest urr4 = new UserRegistrationRequest("aaa", "aaa", "aaa");
        User user4 = userService.saveUser(urr4);
        Post post3 = postRepository.save(new Post("header3", "text3", "null"));
        user4.addPostToUser(post3);

        UserRegistrationRequest urr5 = new UserRegistrationRequest("bbb", "bbb", "bbb");
        User user5 = userService.saveUser(urr5);
        Post post4 = postRepository.save(new Post("header4", "text4", "null"));
        user5.addPostToUser(post4);


        user1.addSubscriptionToUser(user2);
        user1.addSubscriptionToUser(user3);
        user1.addSubscriptionToUser(user4);
        user1.addSubscriptionToUser(user5);
        userService.saveUserByEntity(user1);


        //when
        ResultActions resultActions = mockMvc.perform(get("/feed")
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ActivityFeedRequest(2,2))));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagesAmount", is(2)))
                .andExpect(jsonPath("$.posts.size()", is(2)));
    }



    @Test
    @DisplayName("GET /feed by user with subscriptions for user with no posts")
    public void givenUserWithSubscriptionForUsersWithNoPosts_whenGetActivityFeed_thenReturns404() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);

        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);

        UserRegistrationRequest urr3 = new UserRegistrationRequest("zzz", "zzz", "zzz");
        User user3 = userService.saveUser(urr3);
        user1.addSubscriptionToUser(user2);
        user1.addSubscriberToUser(user3);
        userService.saveUserByEntity(user1);


        //when
        ResultActions resultActions = mockMvc.perform(get("/feed")
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ActivityFeedRequest(2,1))));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagesAmount", is(1)));
//                .andExpect(jsonPath("$.posts", is("null"), String.class));
    }
}
