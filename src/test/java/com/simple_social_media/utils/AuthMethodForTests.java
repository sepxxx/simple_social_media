package com.simple_social_media.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple_social_media.dtos.requests.UserRegistrationRequest;
import com.simple_social_media.dtos.responses.JwtResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthMethodForTests {

    public static String getToken(UserRegistrationRequest urr, MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("username", urr.getUsername());
        obj.put("password", urr.getPassword());
        MvcResult authResult = mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(obj.toString()))
                .andExpect(status().isOk())
                .andReturn();

        String jwtJSON = authResult.getResponse().getContentAsString();
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!"+ jwtJSON);
        JwtResponse jwtResponse = objectMapper.readValue(jwtJSON, JwtResponse.class);
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!"+ jwtResponse);
        String jwt = jwtResponse.getToken();
        return "Bearer " + jwt;
    }

}
