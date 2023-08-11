package com.simple_social_media.services;

import com.simple_social_media.dtos.requests.MessageRequest;
import com.simple_social_media.dtos.responses.ConversationResponse;
import com.simple_social_media.dtos.responses.MessageResponse;
import com.simple_social_media.entities.Conversation;
import com.simple_social_media.entities.Message;
import com.simple_social_media.entities.User;
import com.simple_social_media.exceptions.AppError;
import com.simple_social_media.repositories.ConversationRepository;
import com.simple_social_media.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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


    public ResponseEntity<?> getCurrentUserConversations() {
        //C: проверка контекста
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(contextUserName).get();
        List<ConversationResponse> conversationResponses = user.getConversations().stream().map(c ->
                new ConversationResponse(c.getId(), c.getHeader())).toList();
        return ResponseEntity.ok(conversationResponses);
    }

    public ResponseEntity<?> getConversationMessagesById(Long id) {
        // + проверка контекста
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(contextUserName).get();
        //здесь нужна проверка что сообщения запрашивает один из основателей диалога
        Optional<Conversation> conversationOptional = conversationRepository.findById(id);
        if (conversationOptional.isPresent()) {//проверка на существование
            Conversation conversation = conversationOptional.get();
            if (conversation.getUsersList().contains(user)) {//проверка на права доступа
                List<Message> messageList = conversation.getMessageList();
                List<MessageResponse> messageResponseList = messageList.stream().map(m -> new MessageResponse(m.getId(),
                        m.getConversationId(), m.getAuthor_id(), m.getText(), m.getDate())).toList();
                return ResponseEntity.ok(messageResponseList);
            } else {
                return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(),
                        "диалог запрашивает не владелец"),
                        HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    String.format("диалога с id %d не существует", id)),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> sendMessageToUser(MessageRequest messageRequest) {

       //+проверка контекста
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(contextUserName).get();
        //+ проверка существования targetUser, можно добавить метод в advice
        Optional<User> targetUserOptional = userService.findById(messageRequest.getTargetUserId());
        if (targetUserOptional.isPresent()) {
//        User targetUser = userService.findById(messageRequest.getTargetUserId()).get();
            User targetUser = targetUserOptional.get();
            Optional<Conversation> optionalConversation = conversationRepository.findByUsers(user, targetUser);
            if (optionalConversation.isEmpty()) {//если у пользователей нет диалога
                if (friendsAndSubsService.makeUserFriendsList(user).contains(targetUser)) {//проверка на дружбу
                    //этот код должен выполниться когда не было диалога и он создался,
                    //но после создания диалога нужно брать не из optional, а обращаться к созданному объекту
                    Conversation conversation = new Conversation(String.format("conv:%d:%d", user.getId(), messageRequest.getTargetUserId()));
                    conversation.addUserToConversation(user);
                    conversation.addUserToConversation(targetUser);
                    //сначала нужно сохранить, чтобы получить id от бд
                    conversation = conversationRepository.save(conversation);

                    Message message = new Message(conversation.getId(), user.getId(), messageRequest.getText(), new Date());
                    conversation.addMessageToConversation(message);
                    conversationRepository.save(conversation);
                    return ResponseEntity.ok(String.format("сообщение отправлено user id: %d", targetUser.getId()));
                } else {
                    return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(),
                            "создание диалога запрашивает не друг"),
                            HttpStatus.FORBIDDEN);
                }
            } else {
                //когда был диалог
                Conversation conversation = optionalConversation.get();
                Message message = new Message(conversation.getId(), user.getId(), messageRequest.getText(), new Date());
                conversation.addMessageToConversation(message);
                conversationRepository.save(conversation);
                return ResponseEntity.ok(String.format("сообщение отправлено user id: %d", targetUser.getId()));
            }
        } else {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    String.format("user с id %d не существует", messageRequest.getTargetUserId())),
                    HttpStatus.NOT_FOUND);
        }


    }

    public Conversation saveConversationByEntity(Conversation c) {
        return conversationRepository.save(c);
    }
}
