package com.simple_social_media.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="name")
    private String username;
    @Column(name="mail")
    private String email;
    @Column(name="password")
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
    private List<User> subscriptions;

//    @ManyToMany
//    @JoinTable(name="subscribes",
//            joinColumns=@JoinColumn(name="target_id"),
//            inverseJoinColumns=@JoinColumn(name="source_id")
//    )
    @ManyToMany(mappedBy="subscriptions")
    private List<User> subscribers;


    @ManyToMany
    @JoinTable(name = "users_roles",
    joinColumns = @JoinColumn(name="user_id"),
    inverseJoinColumns = @JoinColumn(name="role_id"))
    private Collection<Role> roles;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="conversations_users",
    joinColumns = @JoinColumn(name="user_id"),
    inverseJoinColumns = @JoinColumn(name="conversation_id"))
    private List<Conversation> conversations;

    public void addSubscriberToUser(User user) {
        if(subscribers==null)
            subscribers = new ArrayList<>();
        subscribers.add(user);
    }

    public void addSubscriptionToUser(User user) {
        if(subscriptions==null)
            subscriptions = new ArrayList<>();
        subscriptions.add(user);
     }


    public void addPostToUser(Post post) {
        if(posts==null)
            posts = new ArrayList<>();
        posts.add(post);

    }
    public User(String name, String mail, String password) {
        this.username = name;
        this.email = mail;
        this.password = password;
    }

    public User() {

    }
}
