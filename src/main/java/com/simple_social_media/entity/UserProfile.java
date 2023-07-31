package com.simple_social_media.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name="user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String name;
    @Column
    private String mail;
    @Column
    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_post",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="post_id"))
    private List<Post> posts;



    @ManyToMany
    @JoinTable(name="subscribes",
            joinColumns=@JoinColumn(name="source_id"),
            inverseJoinColumns=@JoinColumn(name="target_id")
    )
    private List<UserProfile> subscriptions;

    @ManyToMany
    @JoinTable(name="subscribes",
            joinColumns=@JoinColumn(name="target_id"),
            inverseJoinColumns=@JoinColumn(name="source_id")
    )
    private List<UserProfile> subscribers;

    public List<Post> getPosts() {
        return posts;
    }

    public List<UserProfile> getSubscriptions() {
        return subscriptions;
    }


    public List<UserProfile> getSubscribers() {
        return subscribers;
    }
    public void addSubscriberToUser(UserProfile userProfile) {
        if(subscribers==null) {
            subscribers = new ArrayList<>();
        } else {
            subscribers.add(userProfile);
        }
    }

    public void addSubscriptionToUser(UserProfile userProfile) {
        if(subscriptions==null) {
            subscriptions = new ArrayList<>();
        }else {
            subscriptions.add(userProfile);
        }
     }


    public void addPostToUser(Post post) {
        if(posts==null) {
            posts = new ArrayList<>();
        } else {
            posts.add(post);
        }
    }
    public UserProfile(String name, String mail, String password) {
        this.name = name;
        this.mail = mail;
        this.password = password;
    }

    public UserProfile() {

    }
}
