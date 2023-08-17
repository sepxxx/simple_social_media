package com.SocialMediaApi.entities;

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
    @JoinTable(name = "users_posts",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="post_id"))
    private List<Post> posts;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="subscriptions",
            joinColumns=@JoinColumn(name="source_id"),
            inverseJoinColumns=@JoinColumn(name="target_id")
    )
    private List<User> subscriptions;

//    @ManyToMany
//    @JoinTable(name="subscribes",
//            joinColumns=@JoinColumn(name="target_id"),
//            inverseJoinColumns=@JoinColumn(name="source_id")
//    )
    //при таких связях замечено в тестах, что
    //при сохранении подписок у юзера setSubscribers или же addSubscriberToUser
    //таблица subscriptions не заполняется
    //нужно у тех кого хотим добавить в подписчики сформировать подписки и все будет ок
    @ManyToMany(mappedBy="subscriptions", cascade = CascadeType.ALL)
    private List<User> subscribers;





//    @ManyToMany(cascade = CascadeType.ALL)
    //F: если оставлю каскад, то не только jointable будет чиститься, но и roles
    //в текущем случае я не могу использовать метод addRoleToUser, а могу лишь сеттить список
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



    @PreRemove
    private void preRemove() {
        for (Conversation conversation : conversations) {
            conversation.setMessageList(null);
        }
    }

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

    public void addRoleToUser(Role role) {
        if(roles==null)
            roles = new ArrayList<>();
        roles.add(role);
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
