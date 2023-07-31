package com.simple_social_media.services;

import com.simple_social_media.entity.Post;
import com.simple_social_media.entity.UserProfile;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserProfileService {

        List<UserProfile> getAllUserProfiles();
        void saveUserProfile(UserProfile UserProfile);

        UserProfile getUserProfile(int id);

        void deleteUserProfile(int id);

        List<Post> getAllUserProfilePosts(int id);

        List<UserProfile> getAllUserProfileSubscriptions(int id);
        List<UserProfile> getAllUserProfileSubscribes(int id);

}