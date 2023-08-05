package com.simple_social_media.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="conversations")
@Data

public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
//    @Column(name="uid1")
//    private Long uid1;
//    @Column(name="uid2")
//    private Long uid2;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<Message> messageList;


    @ManyToOne
    @JoinColumn(name="uid1")
    private User user1;

    @ManyToOne
    @JoinColumn(name="uid2")
    private User user2;
    public Conversation() {

    }
}
