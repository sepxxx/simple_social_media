package com.simple_social_media.services;

import com.simple_social_media.dtos.responses.ConversationResponse;
import com.simple_social_media.entities.Conversation;
import com.simple_social_media.entities.User;
import com.simple_social_media.repositories.ConversationRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {
    // Друзья могут писать друг другу сообщения
    // (реализация чата не нужна, пользователи могу запросить переписку с помощью запрос а)

    //1)сообщения принадлежат конкретной беседе
    //2)для отправки сообщения необходим (текст, id беседы, author_id)
    //3)любой не может оправить сообщение в беседу, он должен принадлежать ей
    //4)после отписки дружба пропадает => нельзя писать сообщения
    //но удалять беседу нельзя, тк пропадут сообщения,
    // поэтому перед каждой отправкой сообщения придется проверять дружбу

    //1)для создания беседы необходимы (uid1, uid2)
    //2)source_id можно взять из контекста
    //3)создать беседу могут только друзья

    //юзеру будет необходимо получать все свои беседы
    private final ConversationRepository conversationRepository;
    private final UserService userService;

    private final FriendsAndSubsService friendsAndSubsService;

    public ResponseEntity<?> createConversation(Long targetUserId) {
        //необходима проверка на существование targetUserId
        // + проверка контекста
        // + проверка не существует ли уже такой беседы
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(contextUserName).get();
        List<User> friends =  friendsAndSubsService.makeUserFriendsList(user);
        User targetUser = userService.findById(targetUserId).get();

        if(friends.contains(targetUser)) {//проверка на дружбу
            Conversation conversation = new Conversation(String.format("conv:%d:%d",user.getId(), targetUserId));
//          таким образом создается 2 диалога
//            user.getConversations().add(conversation);
//            targetUser.getConversations().add(conversation);
//            userService.saveUserByEntity(user);

            conversation.addUserToConversation(user);
            conversation.addUserToConversation(targetUser);
            conversationRepository.save(conversation);

            return ResponseEntity.ok(String.format("создан диалог с id %d", targetUserId));
        } else {
            return new ResponseEntity<>(String.format("вы не друзья с id %d", targetUserId), HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> getCurrentUserConversations() {
        //необходима проверка на существование targetUserId
        // + проверка контекста
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(contextUserName).get();
        List<ConversationResponse> conversationResponses = user.getConversations().stream().map(c->
                new ConversationResponse(c.getId(), c.getHeader())).toList();
        return ResponseEntity.ok(conversationResponses);
    }

}
