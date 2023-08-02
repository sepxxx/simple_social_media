package com.simple_social_media.services;

import com.simple_social_media.dtos.requests.UserRegistrationRequest;
import com.simple_social_media.entities.Post;
import com.simple_social_media.entities.User;
import com.simple_social_media.repositories.RoleRepository;
import com.simple_social_media.repositories.UserRepository;
import com.simple_social_media.security.CustomUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

//
//    public void saveUser(User user) {
//        userRepository.save(user);
//    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User getUser(Long id) {
        Optional<User> optional = userRepository.findById(id);
        User user = null;
        if(optional.isPresent())
            user =optional.get();
        return user;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    public List<Post> getAllUserPosts(Long id) {
        //используем уже готовый метод работающий с репозиторием
        User user = getUser(id);
        if(user ==null) return null;
        return user.getPosts();
    }


    public List<User> getAllUserSubscriptions(Long id) {
        //используем уже готовый метод работающий с репозиторием
        User user = getUser(id);
        if(user ==null) return null;
        return user.getSubscriptions();
    }

    public List<User> getAllUserSubscribes(Long id) {
        //используем уже готовый метод работающий с репозиторием
        User user = getUser(id);
        if(user ==null) return null;
        return user.getSubscribers();
    }
}
