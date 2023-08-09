package com.simple_social_media.services;

import com.simple_social_media.dtos.requests.UserRegistrationRequest;
import com.simple_social_media.dtos.responses.PostResponse;
import com.simple_social_media.dtos.responses.UserResponse;
import com.simple_social_media.entities.Post;
import com.simple_social_media.entities.User;
import com.simple_social_media.exceptions.AppError;
import com.simple_social_media.repositories.RoleRepository;
import com.simple_social_media.repositories.UserRepository;
import com.simple_social_media.security.CustomUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    public Boolean existsByName(String username){
        return userRepository.existsByUsername(username);
    }
    public Boolean existsByMail(String mail) {
        return userRepository.existsByEmail(mail);
    }
    public Boolean existsById(Long id) {return userRepository.existsById(id);}

    public User saveUserByEntity(User user) {return userRepository.save(user);}

//    public void deleteAllUsers() { userRepository.deleteAll();}
    public User saveUser(UserRegistrationRequest userRegistrationRequest) {
        User user = new User();
        user.setEmail(userRegistrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
        user.setUsername(userRegistrationRequest.getUsername());
        user.setRoles(List.of(roleService.getUserRole()));
        return userRepository.save(user);
    }

    public ResponseEntity<?> getAllUsers() {
        //C:проводится проверка контекста
        //нужно отмаппить лист юзеров к листу UserResponse(id,email,name)
        //те по факту отбрасываем доп данные юзера
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(contextUserName).get();
        if(user.getRoles().contains(roleService.getAdminRole())) {
            List<User> users = userRepository.findAll();
            List<UserResponse> userResponses = users.stream().map(u -> new UserResponse(u.getId(), u.getUsername(), u.getEmail())).toList();
            return ResponseEntity.ok(userResponses);
        } else {
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(),
                    "запрашивает не администратор"),
                    HttpStatus.FORBIDDEN);
        }
    }


    public ResponseEntity<?> getUserById(Long id) {
        //C:проводится проверка существования юзера
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User user = optional.get();
            return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), user.getEmail()));
        } else {
            return new ResponseEntity<>(String.format("нет юзера с id %d ", id), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> deleteUserById(Long id) {
        //C:проводится проверка контекста
        //проверка существования юзера
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //получаем юзера по id чтобы сверить ники
        //нужно переиспользовать метод из сервиса, чтобы не дублировать код
        //но не знаю можно ли вообще так писать, тк возвращается ResponseEntity
        ResponseEntity<?> responseEntity = getUserById(id);
        if(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK)) {//если нашли юзера можно попробовать удалить
            UserResponse userResponse = (UserResponse) responseEntity.getBody();
            if(userResponse.getUsername().equals(contextUserName)){//если ники контекста и того кого хотим удалить совпадают
                userRepository.deleteById(id);
                return ResponseEntity.ok(String.format("пользователь с id %d был удален", id));
            } else {
                return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(),
                        "удаление запрашивает не сам пользователь"),
                        HttpStatus.FORBIDDEN);
            }
        } else {
            return responseEntity;//иначе возвращаем что такого юзера нет
        }
    }


    public ResponseEntity<?> getUserPostsByUserId(Long id) {

        //C:проводится проверка существования юзера
        //здесь нет смысла использовать методы  existsByName
        //и getUserById тк затем придется обращаться к полю Posts юзера
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User user = optional.get();
            //отмаппим список постов к списку PostResponse
            List<Post> postsList = user.getPosts();
            List<PostResponse> postResponseList = postsList.stream().map(p -> new PostResponse(p.getId(),
                    p.getHeader(),p.getText(),p.getDate(),p.getUser().getUsername(),p.getImage_url())).toList();
            return ResponseEntity.ok(postResponseList);
        } else {
            return new ResponseEntity<>(String.format("нет юзера с id %d ", id), HttpStatus.NOT_FOUND);
        }

    }



    @Override
    @Transactional
    //:F не знаю точно нужно ли transactional, в туторе так
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //найти юзера
        //затем преобразовать к виду, который понимает spring
        //те к UserDetails предоставляющему необходимую информацию для построения объекта Authentication
        User user = findByUsername(username).
                orElseThrow(()->new UsernameNotFoundException(String.
                        format("Пользователь %s не найден",username)));

        return new CustomUser (
                user.getUsername(),
                user.getPassword(),
                //нужно получить роли и отмаппить к виду нужному Spring
                //GrantedAuthority отражает разрешения выданные пользователю в масштабе всего приложения
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()),
                user.getId()
        );
    }

}
