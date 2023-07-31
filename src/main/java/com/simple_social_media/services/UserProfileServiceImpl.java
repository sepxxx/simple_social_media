package com.simple_social_media.services;

import com.simple_social_media.dao.UserProfileRepository;
import com.simple_social_media.entity.Post;
import com.simple_social_media.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService{
    private final UserProfileRepository userProfileRepository;

    @Override
    public List<User> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    @Override
    public void saveUserProfile(User user) {
        userProfileRepository.save(user);
    }

    @Override
    public User getUserProfile(int id) {
        Optional<User> optional = userProfileRepository.findById(id);
        User user = null;
        if(optional.isPresent())
            user =optional.get();
        return user;
    }

    @Override
    public void deleteUserProfile(int id) {
        userProfileRepository.deleteById(id);
    }


    @Override
    public List<Post> getAllUserProfilePosts(int id) {
        //используем уже готовый метод работающий с репозиторием
        User user = getUserProfile(id);
        if(user ==null) return null;
        return user.getPosts();
    }


    @Override
    public List<User> getAllUserProfileSubscriptions(int id) {
        //используем уже готовый метод работающий с репозиторием
        User user = getUserProfile(id);
        if(user ==null) return null;
        return user.getSubscriptions();
    }

    @Override
    public List<User> getAllUserProfileSubscribes(int id) {
        //используем уже готовый метод работающий с репозиторием
        User user = getUserProfile(id);
        if(user ==null) return null;
        return user.getSubscribers();
    }
}
