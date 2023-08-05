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

    // сообщения принадлежат конкретной беседе
    //для отправки сообщения необходим (текст, id беседы)
    //любой не может оправить сообщение в беседу, он должен принадлежать ей
    //после отписки дружба пропадает => нельзя писать сообщения
    //но удалять беседу нельзя, тк пропадут сообщения,
    // поэтому перед каждой отправкой сообщения придется проверять дружбу

    //для создания беседы необходимы (uid1, uid2)
    //source_id можно взять из контекста
    //создать беседу могут только друзья

    //юзеру будет необходимо получать все свои беседы

    private final ConversationRepository conversationRepository;
    private final UserService userService;

    private final FriendsAndSubsService friendsAndSubsService;

    public ResponseEntity<?> createConversation(Long targetUserId) {
        //необходима проверка на существование targetUserId
        // + проверка контекста
        // + проверка не существует ли обратной ситуцаии 3/4 4/3
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(contextUserName).get();
        //формирование списка id друзей поможет не обращаться в базу за объектом targetUser
        //List<Long> friendsIds =  friendsAndSubsService.makeUserFriendsList(user).stream().map(User::getId).toList();
        //но все же придется получить targetUser из базы для формирования conversation

        List<User> friends =  friendsAndSubsService.makeUserFriendsList(user);
        User targetUser = userService.findById(targetUserId).get();
        if(friends.contains(targetUser)) {//проверка на дружбу
            Conversation conversation = new Conversation();



            conversation.setUser1(user);
            conversation.setUser2(targetUser);
//            user.getConversations().add(conversation);
//            targetUser.getConversations().add(conversation);
//            userService.saveUserByEntity(user);
            conversationRepository.save(conversation);
            return ResponseEntity.ok(String.format("создан диалог с id %d", targetUserId));
        } else {
            return new ResponseEntity<>(String.format("вы не друзья с if %d", targetUserId), HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> getCurrentUserConversations() {
        //необходима проверка на существование targetUserId + проверка контекста
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(contextUserName).get();
        List<ConversationResponse> conversationResponses = user.getConversations().stream().map(c->
                new ConversationResponse(c.getId(), c.getUser1().getId(), c.getUser2().getId())).toList();
        return ResponseEntity.ok(conversationResponses);
    }

}
