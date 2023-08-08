package com.simple_social_media.services;


import com.simple_social_media.dtos.requests.JwtRequest;
import com.simple_social_media.dtos.responses.JwtResponse;
import com.simple_social_media.dtos.responses.UserResponse;
import com.simple_social_media.dtos.requests.UserRegistrationRequest;
import com.simple_social_media.entities.User;
import com.simple_social_media.exceptions.AppError;
import com.simple_social_media.security.CustomUser;
import com.simple_social_media.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;

    public ResponseEntity<?> createAuthToken(JwtRequest jwtRequest) {
        //сначала проверим есть ли в базе
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "некорректный логин или пароль"), HttpStatus.BAD_REQUEST);
        }

        //тут в туторе использовался loadUserByUsername который возвращает UserDetails
        //но мне нужен id юзера для генерации Jwt
//        UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
        CustomUser customUser = (CustomUser) userService.loadUserByUsername(jwtRequest.getUsername());
        String token = jwtTokenUtils.generateToken(customUser);
        return ResponseEntity.ok(new JwtResponse(token));
    }


    public ResponseEntity<?> createNewUser(UserRegistrationRequest userRegistrationRequest) {

        if (userService.existsByName(userRegistrationRequest.getUsername()))
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с указанным именем уже существует"), HttpStatus.BAD_REQUEST);
        if(userService.existsByMail(userRegistrationRequest.getEmail()))
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с указанным mail уже существует"), HttpStatus.BAD_REQUEST);
        User user = userService.saveUser(userRegistrationRequest);
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), user.getEmail()));
    }
}
