package com.simple_social_media.services;

import com.simple_social_media.dao.UserProfileRepository;
import com.simple_social_media.entity.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileServiceImpl implements UserProfileService{
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public List<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    @Override
    public void saveUserProfile(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile getUserProfile(int id) {
        Optional<UserProfile> optional = userProfileRepository.findById(id);
        UserProfile userProfile = null;
        if(optional.isPresent())
            userProfile=optional.get();
        return userProfile;
    }

    @Override
    public void deleteUserProfile(int id) {
        userProfileRepository.deleteById(id);
    }

}
