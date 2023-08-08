package com.simple_social_media;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple_social_media.controllers.UserController;
import com.simple_social_media.dtos.requests.UserRegistrationRequest;
import com.simple_social_media.dtos.responses.JwtResponse;
import com.simple_social_media.entities.User;
import com.simple_social_media.repositories.UserRepository;
import com.simple_social_media.services.RoleService;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;


import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();;
    }

    @Test
    @DisplayName("GET /users by not admin returns 403 ")
    public void givenUsersListAndNotAdmin_whenGetAllUsers_returns403() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        userService.saveUser(urr);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        userService.saveUser(urr2);
        UserRegistrationRequest urr3= new UserRegistrationRequest("zzz", "zzz", "zzz");
        userService.saveUser(urr3);
        //when
        ResultActions response = mockMvc.perform(
                get("/users")
                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
        );
        //then
        response.andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.size()", is(3)));
    }

    @Test
    @DisplayName("GET /users by admin returns 200 and list of userDto ")
    public void givenUsersListAndAdmin_whenGetAllUsers_returnsListUserDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user = userService.saveUser(urr);
        user.setRoles(List.of(roleService.getUserRole(), roleService.getAdminRole()));
        userService.saveUserByEntity(user);
        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        userService.saveUser(urr2);
        UserRegistrationRequest urr3= new UserRegistrationRequest("zzz", "zzz", "zzz");
        userService.saveUser(urr3);
        //when
        ResultActions response = mockMvc.perform(
                get("/users")
                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
        );
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(3)));
    }

    @Test
    @DisplayName("GET /users/id with valid id returns UserDto")
    public void givenValidId_whenGetUserById_returnsUserDto() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user = userService.saveUser(urr);
        //when
        ResultActions response = mockMvc.perform(
                get("/users/{id}", user.getId())
                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
        );
        //then
        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.username",
                        is(user.getUsername())))
                .andExpect(jsonPath("$.email",
                        is(user.getEmail())))
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    @DisplayName("GET /users/id invalid id returns 404")
    public void givenInvalidId_whenGetUserById_returns404() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user = userService.saveUser(urr);
        //when
        ResultActions response = mockMvc.perform(
                get("/users/{id}", 777)
                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
        );
        //then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }






}
