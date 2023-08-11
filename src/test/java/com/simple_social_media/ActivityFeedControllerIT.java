package com.simple_social_media;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple_social_media.dtos.requests.ActivityFeedRequest;
import com.simple_social_media.dtos.requests.UserRegistrationRequest;
import com.simple_social_media.entities.Post;
import com.simple_social_media.entities.User;
import com.simple_social_media.repositories.PostRepository;
import com.simple_social_media.repositories.UserRepository;
import com.simple_social_media.services.PostService;
import com.simple_social_media.services.UserService;
import com.simple_social_media.utils.AuthMethodForTests;
import org.json.JSONObject;
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
public class ActivityFeedControllerIT {
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
                .andExpect(status().isNotFound());
    }



    @Test
    @DisplayName("GET /feed by user with subscriptions for users with posts WHEN Nof posts = limit")
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

//    @Test
//    @DisplayName("GET /feed by user with subscriptions for users with posts")
//    public void givenUserWithSubscriptions_whenGetActivityFeed_thenReturnsListOfPostDtos() throws Exception {
//        //given
//        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
//        User user1 = userService.saveUser(urr);
//
//        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
//        User user2 = userService.saveUser(urr2);
//        Post post = postRepository.save(new Post("header1", "text1", "null"));
//        user2.addPostToUser(post);
//
//        UserRegistrationRequest urr3 = new UserRegistrationRequest("zzz", "zzz", "zzz");
//        User user3 = userService.saveUser(urr3);
//        Post post2 = postRepository.save(new Post("header2", "text2", "null"));
//        user3.addPostToUser(post2);
//
//        user1.addSubscriptionToUser(user2);
//        user1.addSubscriptionToUser(user3);
//        userService.saveUserByEntity(user1);
//
//
//        //when
//        ResultActions resultActions = mockMvc.perform(get("/feed")
//                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(new ActivityFeedRequest(2,1))));
//        //then
//        resultActions.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()", is(2)));
//    }

}
