package com.SocialMediaApi.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name="messages")
@Data

public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="conversation_id")
    private Long conversationId;
    @Column(name="author_id")
    private Long author_id;
    @Column(name="text")
    private String text;
    @Column(name="date")
    private Date date;

    public Message() {

    }

    public Message(Long conversationId, Long author_id, String text, Date date) {
        this.conversationId = conversationId;
        this.author_id = author_id;
        this.text = text;
        this.date = date;
    }
}
