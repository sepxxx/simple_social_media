package com.simple_social_media.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String header;
    @Column
    private String text;
    @Column
    private String image_url;


    @ManyToOne
    @JoinTable(name = "user_post",
            joinColumns = @JoinColumn(name="post_id"),
            inverseJoinColumns = @JoinColumn(name="user_id"))
    private User user;

    public Post() {
    }

    public Post(String header, String text, String image_url) {
        this.header = header;
        this.text = text;
        this.image_url = image_url;
    }
}
