package com.simple_social_media.services;

import com.simple_social_media.entity.Post;
import com.simple_social_media.entity.User;

import java.util.List;


public interface UserProfileService {

        List<User> getAllUserProfiles();
        void saveUserProfile(User User);

        User getUserProfile(int id);

        void deleteUserProfile(int id);

        List<Post> getAllUserProfilePosts(int id);

        List<User> getAllUserProfileSubscriptions(int id);
        List<User> getAllUserProfileSubscribes(int id);

}
