package com.simple_social_media.services;


import com.simple_social_media.dtos.JwtRequestDto;
import com.simple_social_media.dtos.JwtResponseDto;
import com.simple_social_media.dtos.UserDto;
import com.simple_social_media.dtos.UserRegistrationDto;
import com.simple_social_media.entities.User;
import com.simple_social_media.exceptions.AppError;
import com.simple_social_media.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;

    public ResponseEntity<?> createAuthToken( JwtRequestDto jwtRequestDto) {
        //сначала проверим есть ли в базе
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequestDto.getUsername(), jwtRequestDto.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "некорректный логин или пароль"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.findByUsername(jwtRequestDto.getUsername());
        String token  = jwtTokenUtils.generateToken(user);
        return ResponseEntity.ok(new JwtResponseDto(token));
    }


//    public ResponseEntity<?> createNewUser( UserRegistrationDto userRegistrationDto) {
//        if(userService.findByUsername(userRegistrationDto.getUsername())!=null) {
//            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "пользователь с таким именем уже существует"), HttpStatus.BAD_REQUEST);
//        }
//        User user = userService.saveUser(userRegistrationDto);
//        return ResponseEntity.ok(new UserDto(user.getId(), user.getName(), user.getMail()));
//    }


    public ResponseEntity<?> createNewUser(UserRegistrationDto userRegistrationDto) {

        if (userService.existsByName(userRegistrationDto.getUsername())) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с указанным именем уже существует"), HttpStatus.BAD_REQUEST);
        }
        User user = userService.saveUser(userRegistrationDto);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getName(), user.getMail()));
    }
}
