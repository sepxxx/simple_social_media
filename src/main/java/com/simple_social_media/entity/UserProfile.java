package com.simple_social_media.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import com.simple_social_media.entity.Post;

@Entity
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
