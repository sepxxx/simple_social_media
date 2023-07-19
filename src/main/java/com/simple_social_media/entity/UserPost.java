package com.simple_social_media.entity;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="user_post")
public class UserPost {
    @Column
    private int user_id;
    @Column
    private int post_id;
    @Column
    private Date date;

    public UserPost() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UserPost(int user_id, int post_id, Date date) {
        this.user_id = user_id;
        this.post_id = post_id;
        this.date = date;
    }
}
