package com.simple_social_media;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple_social_media.dtos.requests.UserRegistrationRequest;
import com.simple_social_media.entities.User;
import com.simple_social_media.repositories.UserRepository;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class FriendsAndSubsControllerIT {
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
    @DisplayName("GET /users/id/subscribers with invalid id")
    public void givenInvalidId_whenGetUserSubscribersById_thenReturns404() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        userService.saveUser(urr);
        //when
        ResultActions resultActions = mockMvc.perform(get("/users/{id}/subscribers", 1234567890)
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /users/id/subscribers with valid id by user with same id")
    public void givenValidIdSameUser_whenGetUserSubscribersById_thenReturnsListOfUsersDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        UserRegistrationRequest urr3 = new UserRegistrationRequest("zzz", "zzz", "zzz");
        User user3 = userService.saveUser(urr3);

        user2.addSubscriptionToUser(user1);
        user3.addSubscriptionToUser(user1);
//        user2.setSubscriptions(List.of(user1));
//        user3.setSubscriptions(List.of(user1));
        userService.saveUserByEntity(user2);
        userService.saveUserByEntity(user3);

        //when
        ResultActions resultActions = mockMvc.perform(get("/users/{id}/subscribers", user1.getId())
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }


    @Test
    @DisplayName("GET /users/id/subscribers with valid id by user with another id")
    public void givenValidIdAnotherUser_whenGetUserSubscribersById_thenReturnsListOfUsersDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        UserRegistrationRequest urr3 = new UserRegistrationRequest("zzz", "zzz", "zzz");
        User user3 = userService.saveUser(urr3);
        UserRegistrationRequest urr4 = new UserRegistrationRequest("sss", "sss", "sss");
        User user4 = userService.saveUser(urr4);


        user2.addSubscriptionToUser(user1);
        user3.addSubscriptionToUser(user1);
//        user2.setSubscriptions(List.of(user1));
//        user3.setSubscriptions(List.of(user1));
        userService.saveUserByEntity(user2);
        userService.saveUserByEntity(user3);

        //when
        ResultActions resultActions = mockMvc.perform(get("/users/{id}/subscribers", user1.getId())
                .header("Authorization", AuthMethodForTests.getToken(urr4, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    @DisplayName("GET /users/id/subscriptions with invalid id")
    public void givenInvalidId_whenGetUserSubscriptionsById_thenReturns404() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        userService.saveUser(urr);
        //when
        ResultActions resultActions = mockMvc.perform(get("/users/{id}/subscriptions", 1234567890)
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /users/id/subscriptions with valid id by user with same id")
    public void givenValidIdSameUser_whenGetUserSubscriptionsById_thenReturnsListOfUsersDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        UserRegistrationRequest urr3 = new UserRegistrationRequest("zzz", "zzz", "zzz");
        User user3 = userService.saveUser(urr3);
        user1.setSubscriptions(List.of(user2, user3));
        userService.saveUserByEntity(user1);
        //when
        ResultActions resultActions = mockMvc.perform(get("/users/{id}/subscriptions", user1.getId())
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    @DisplayName("GET /users/id/subscriptions with valid id by user with another id")
    public void givenValidIdAnotherUser_whenGetUserSubscriptionsById_thenReturnsListOfUsersDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        UserRegistrationRequest urr3 = new UserRegistrationRequest("zzz", "zzz", "zzz");
        User user3 = userService.saveUser(urr3);
        UserRegistrationRequest urr4 = new UserRegistrationRequest("lll", "lll", "lll");
        User user4 = userService.saveUser(urr4);
        user1.setSubscriptions(List.of(user2, user3));
        userService.saveUserByEntity(user1);
        //when
        ResultActions resultActions = mockMvc.perform(get("/users/{id}/subscriptions", user1.getId())
                .header("Authorization", AuthMethodForTests.getToken(urr4, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }



    @Test
    @DisplayName("GET /users/id/friends with invalid id")
    public void givenInvalidId_whenGetUserFriendsById_thenReturns404() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        userService.saveUser(urr);
        //when
        ResultActions resultActions = mockMvc.perform(get("/users/{id}/friends", 1234567890)
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("GET /users/id/friends with valid id by user with same id" +
            " + current user has 3 subscriptions/2subscribers")
    public void givenValidIdSameUser_whenGetUserFriendsById_thenReturnsListOfUsersDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        UserRegistrationRequest urr3 = new UserRegistrationRequest("zzz", "zzz", "zzz");
        User user3 = userService.saveUser(urr3);
        UserRegistrationRequest urr4 = new UserRegistrationRequest("lll", "lll", "lll");
        User user4 = userService.saveUser(urr4);
        user1.setSubscriptions(List.of(user2, user3, user4));
        user2.addSubscriptionToUser(user1);
        user3.addSubscriptionToUser(user1);
        userService.saveUserByEntity(user2);
        userService.saveUserByEntity(user1);
        userService.saveUserByEntity(user3);
        //when
        ResultActions resultActions = mockMvc.perform(get("/users/{id}/friends", user1.getId())
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
//                .andExpect(jsonPath("$"));
    }

    @Test
    @DisplayName("GET /users/id/friends with valid id no friends by user with another id ")
    public void givenValidIdAnotherUser_whenGetUserFriendsById_thenReturnsListOfUsersDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        userService.saveUser(urr2);
        //when
        ResultActions resultActions = mockMvc.perform(get("/users/{id}/friends", user1.getId())
                .header("Authorization", AuthMethodForTests.getToken(urr2, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }


    @Test
    @DisplayName("GET /users/me/friendRequests no requests")
    public void givenNoFR_whenGetUserFriendRequests_thenReturnsListOfUsersDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);
//        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
//        userService.saveUser(urr2);
        //when
        ResultActions resultActions = mockMvc.perform(get("/users/me/friendRequests", user1.getId())
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    @DisplayName("GET /users/me/friendRequests 2 not mutual subscribers=2friend requests")
    public void given2FR_whenGetUserFriendRequests_thenReturnsListOfUsersDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        UserRegistrationRequest urr3 = new UserRegistrationRequest("zzz", "zzz", "zzz");
        User user3 = userService.saveUser(urr3);
        user2.addSubscriptionToUser(user1);
        user3.addSubscriptionToUser(user1);
        userService.saveUserByEntity(user2);
        userService.saveUserByEntity(user3);
        //when
        ResultActions resultActions = mockMvc.perform(get("/users/me/friendRequests", user1.getId())
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    @DisplayName("GET /users/me/friendRequests 1 mutual subscriber, 1 not mutual=1friend requests")
    public void given1FR_whenGetUserFriendRequests_thenReturnsListOfUsersDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        UserRegistrationRequest urr3 = new UserRegistrationRequest("zzz", "zzz", "zzz");
        User user3 = userService.saveUser(urr3);
        user1.addSubscriptionToUser(user2);
        user2.addSubscriptionToUser(user1);
        user3.addSubscriptionToUser(user1);
        userService.saveUserByEntity(user1);
        userService.saveUserByEntity(user2);
        userService.saveUserByEntity(user3);
        //when
        ResultActions resultActions = mockMvc.perform(get("/users/me/friendRequests", user1.getId())
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    @DisplayName("POST /users/{id}/subscribers invalid id returns 404")
    public void givenInvalidId_whenSubscribeById_thenReturns404() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);

        //when
        ResultActions resultActions = mockMvc.perform(post("/users/{id}/subscribers", 1234567890)
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /users/{id}/subscribers valid id request by user with same id returns 400")
    public void givenValidIdSameUser_whenSubscribeById_thenReturns400() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);
        //when
        ResultActions resultActions = mockMvc.perform(post("/users/{id}/subscribers", user1.getId())
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users/{id}/subscribers valid id request by user already following returns 400")
    public void givenValidIdUserAlreadyFollowing_whenSubscribeById_thenReturns400() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        user1.addSubscriptionToUser(user2);
        userService.saveUserByEntity(user1);
        //when
        ResultActions resultActions = mockMvc.perform(post("/users/{id}/subscribers", user2.getId())
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users/{id}/subscribers valid id request by user not following returns 200")
    public void givenValidId_whenSubscribeById_thenReturns200() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        User user2 = userService.saveUser(urr2);
        //when
        ResultActions resultActions = mockMvc.perform(post("/users/{id}/subscribers", user2.getId())
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }



    @Test
    @DisplayName("DELETE /users/{id}/subscribers invalid id returns 404")
    public void givenInvalidId_whenUnsubscribeById_thenReturns404() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);
        //when
        ResultActions resultActions = mockMvc.perform(delete("/users/{id}/subscribers", 1234567890)
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /users/{id}/subscribers valid id not following returns 400")
    public void givenValidIdNotFollowing_whenUnsubscribeById_thenReturns400() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("eee", "eee", "eee");
        User user2 = userService.saveUser(urr2);
        //when
        ResultActions resultActions = mockMvc.perform(delete("/users/{id}/subscribers", user2.getId())
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /users/{id}/subscribers valid id following returns 200")
    public void givenValidIdFollowing_whenUnsubscribeById_thenReturns200() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user1 = userService.saveUser(urr);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("eee", "eee", "eee");
        User user2 = userService.saveUser(urr2);
        user1.addSubscriptionToUser(user2);
        userService.saveUserByEntity(user1);
        //when
        ResultActions resultActions = mockMvc.perform(delete("/users/{id}/subscribers", user2.getId())
                .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper)));
        //then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }

}
