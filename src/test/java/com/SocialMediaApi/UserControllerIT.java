package com.SocialMediaApi;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.SocialMediaApi.dtos.requests.UserRegistrationRequest;
import com.SocialMediaApi.entities.Post;
import com.SocialMediaApi.entities.User;
import com.SocialMediaApi.repositories.UserRepository;
import com.SocialMediaApi.services.RoleService;
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


import java.util.List;

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

    @Test
    @DisplayName("DELETE /users/id invalid id returns 404")
    public void givenInvalidId_whenDeleteUserById_returns404() throws Exception {
        //given
        UserRegistrationRequest urr = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user = userService.saveUser(urr);
        //when
        ResultActions response = mockMvc.perform(
                delete("/users/{id}", 777)
                        .header("Authorization", AuthMethodForTests.getToken(urr, mockMvc, objectMapper))
        );
        //then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /users/id valid id request from another id returns 403")
    public void givenValidIdAnotherUserAuth_whenDeleteUserById_returns403() throws Exception {
        //given
        UserRegistrationRequest urr1 = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User userToDelete= userService.saveUser(urr1);

        UserRegistrationRequest urr2 = new UserRegistrationRequest("yyy", "yyy", "yyy");
        userService.saveUser(urr2);

        //when
        ResultActions response = mockMvc.perform(
                delete("/users/{id}", userToDelete.getId())
                        .header("Authorization", AuthMethodForTests.getToken(urr2, mockMvc, objectMapper))
        );
        //then
        response.andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /users/id valid id request from same id returns 200")
    public void givenValidIdSameUserAuth_whenDeleteUserById_returns200() throws Exception {
        //given
        UserRegistrationRequest urr1 = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User userToDelete= userService.saveUser(urr1);


        //when
        ResultActions response = mockMvc.perform(
                delete("/users/{id}", userToDelete.getId())
                        .header("Authorization", AuthMethodForTests.getToken(urr1, mockMvc, objectMapper))
        );
        //then
        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /users/id/posts invalid id returns 404")
    public void givenInvalidId_whenGetUserPostsByUserId_returns404() throws Exception {
        //given
        UserRegistrationRequest urr1 = new UserRegistrationRequest("xxx", "xxx", "xxx");
        userService.saveUser(urr1);
        //when
        ResultActions response = mockMvc.perform(
                get("/users/{id}/posts", 777)
                        .header("Authorization", AuthMethodForTests.getToken(urr1, mockMvc, objectMapper))
        );
        //then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /users/id/posts valid id returns list od PostDto")
    public void givenValidId_whenGetUserPostsByUserId_returnsListOfPostDto() throws Exception {
        //given
        UserRegistrationRequest urr1 = new UserRegistrationRequest("xxx", "xxx", "xxx");
        User user = userService.saveUser(urr1);
        user.setPosts(List.of(new Post("header1", "text1", "url1"),
                new Post("header2", "text2", "url2"), new Post("header3", "text3", "url3")));
        userService.saveUserByEntity(user);
        //when
        ResultActions response = mockMvc.perform(
                get("/users/{id}/posts", user.getId())
                        .header("Authorization", AuthMethodForTests.getToken(urr1, mockMvc, objectMapper))
        );
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(3)));
    }

}
