package com.simple_social_media.entities;


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

//    @Column(name="conversation_id", insertable=false, updatable=false)
//    private Long conversationId;

    @Column(name="text")
    private String text;
    @Column(name="date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
}
