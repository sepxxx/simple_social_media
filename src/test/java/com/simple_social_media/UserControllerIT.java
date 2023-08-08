package com.simple_social_media;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple_social_media.controllers.UserController;
import com.simple_social_media.dtos.requests.UserRegistrationRequest;
import com.simple_social_media.dtos.responses.JwtResponse;
import com.simple_social_media.repositories.UserRepository;
import com.simple_social_media.services.UserService;
import com.simple_social_media.utils.AuthMethodForTests;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;


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
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();;
    }

    @Test
    public void givenUsersList_whenGetAllUsers_returnListUsersDto() throws Exception {
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(3)));
    }
}
