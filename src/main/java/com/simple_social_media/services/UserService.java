package com.simple_social_media.services;

import com.simple_social_media.dtos.requests.UserRegistrationRequest;
import com.simple_social_media.dtos.responses.UserResponse;
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
    //F:здесь лучше инжектить сервис для ролей
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByName(username);
    }

    public Boolean existsByName(String username){
        return userRepository.existsByName(username);
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
                user.getName(),
                user.getPassword(),
                //нужно получить роли и отмаппить к виду нужному Spring
                //GrantedAuthority отражает разрешения выданные пользователю в масштабе всего приложения
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()),
                user.getId()
        );
    }


    public User saveUser(UserRegistrationRequest userRegistrationRequest) {
        User user = new User();
        user.setMail(userRegistrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
        user.setName(userRegistrationRequest.getUsername());

        //F: возвращается optional и хорошо бы обработать
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").get()));
        return userRepository.save(user);
    }

    public ResponseEntity<?> getAllUsers() {
        //нужно отмаппить лист юзеров к листу UserResponse(id,email,name)
        //те по факту отбрасываем доп данные юзера
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = users.stream().map(user -> new UserResponse(user.getId(), user.getName(), user.getMail())).toList();
        return ResponseEntity.ok(userResponses);
    }


    public ResponseEntity<?> getUserById(Long id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User user = optional.get();
            return ResponseEntity.ok(new UserResponse(user.getId(), user.getName(), user.getMail()));
        }
        else {
            return new ResponseEntity<>(String.format("нет юзера с id %d ", id), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> deleteUserById(Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(null != securityContext.getAuthentication()){
            String contextUserName = (String) securityContext.getAuthentication().getPrincipal();
            //получаем юзера по id чтобы сверить ники
            //нужно переиспользовать метод из сервиса, чтобы не дублировать код
            //но не знаю можно ли вообще так писать, тк возвращается ResponseEntity
            ResponseEntity<?> responseEntity = getUserById(id);
            if(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK)) {//если нашли юзера можно попробовать удалить
                UserResponse userResponse = (UserResponse) responseEntity.getBody();
                if(userResponse.getUsername().equals(contextUserName))//если ники контекста и того кого хотим удалить совпадают
                {
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


        } else {
            //стоит обработать ошибку по authentication
            //но непонятно как на данном этапе она может быть пустой
            return new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "securityContext.getAuthentication()=null, невозможно установить владельца поста"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    public List<Post> getAllUserPosts(Long id) {
//        //используем уже готовый метод работающий с репозиторием
//        User user = getUser(id);
//        if(user ==null) return null;
//        return user.getPosts();
//    }
//
//
//    public List<User> getAllUserSubscriptions(Long id) {
//        //используем уже готовый метод работающий с репозиторием
//        User user = getUser(id);
//        if(user ==null) return null;
//        return user.getSubscriptions();
//    }
//
//    public List<User> getAllUserSubscribes(Long id) {
//        //используем уже готовый метод работающий с репозиторием
//        User user = getUser(id);
//        if(user ==null) return null;
//        return user.getSubscribers();
//    }
}
