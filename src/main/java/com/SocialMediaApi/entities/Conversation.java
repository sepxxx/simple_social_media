package com.SocialMediaApi.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="conversations")
@Data

public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="header")
    private String header;

    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name="conversation_id")//таким образом hibernate не будет создавать JoinTable, а
    private List<Message> messageList; //организует связь через ссылочное поле в message

//    @ManyToMany(cascade = CascadeType.ALL)
    @ManyToMany
    @JoinTable(name="conversations_users",
            joinColumns = @JoinColumn(name="conversation_id"),
            inverseJoinColumns = @JoinColumn(name="user_id"))
    private List<User> usersList;



//    @PreRemove
//    private void preRemove() {
//        setMessageList(null);
//    }

    public void addUserToConversation(User user) {
        if(usersList==null){
            usersList = new ArrayList<>();
        }
        usersList.add(user);
    }
    public void addMessageToConversation(Message message) {
        if(messageList==null){
            messageList = new ArrayList<>();
        }
        messageList.add(message);
    }

    public Conversation() {

    }

    public Conversation(String header) {
        this.header = header;
    }
}
