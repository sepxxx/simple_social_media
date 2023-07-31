package com.simple_social_media.services;

import com.simple_social_media.entity.Post;
import com.simple_social_media.entity.User;
import com.simple_social_media.repositories.RoleRepository;
import com.simple_social_media.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public Optional<User> findByUsername(String username) {
        return userRepository.findByName(username);
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

        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                //нужно получить роли и отмаппить к виду нужному Spring
                //GrantedAuthority отражает разрешения выданные пользователю в масштабе всего приложения
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }


    public void saveUser(User user) {
        //F: возвращается optional и хорошо бы обработать
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").get()));
        userRepository.save(user);
    }




    public List<User> getAllUserProfiles() {
        return userRepository.findAll();
    }

    public void saveUserProfile(User user) {
        userRepository.save(user);
    }

    public User getUserProfile(Long id) {
        Optional<User> optional = userRepository.findById(id);
        User user = null;
        if(optional.isPresent())
            user =optional.get();
        return user;
    }

    public void deleteUserProfile(Long id) {
        userRepository.deleteById(id);
    }


    public List<Post> getAllUserProfilePosts(Long id) {
        //используем уже готовый метод работающий с репозиторием
        User user = getUserProfile(id);
        if(user ==null) return null;
        return user.getPosts();
    }


    public List<User> getAllUserProfileSubscriptions(Long id) {
        //используем уже готовый метод работающий с репозиторием
        User user = getUserProfile(id);
        if(user ==null) return null;
        return user.getSubscriptions();
    }

    public List<User> getAllUserProfileSubscribes(Long id) {
        //используем уже готовый метод работающий с репозиторием
        User user = getUserProfile(id);
        if(user ==null) return null;
        return user.getSubscribers();
    }
}
