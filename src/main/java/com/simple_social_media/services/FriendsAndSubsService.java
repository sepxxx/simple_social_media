package com.simple_social_media.services;

import com.simple_social_media.dtos.responses.UserResponse;
import com.simple_social_media.entities.User;
import com.simple_social_media.exceptions.AppError;
import com.simple_social_media.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendsAndSubsService {
    //1)Отправка запроса дружбы по факту представляет собой добавление target_user в список своих подписок
//+ добавление source_user в список подписчиков target_user
//2)Отмена подписки означает удаление source_user из подписчиков target_user, target_user из подписок source_user
//3)Проверка активных заявок означает возврат списка подписчиков минус подписок
//4)Принять запрос дружбы означает подписаться в ответ
//5)проверка списка друзей означает возврат пересечения списков подписок и подписчиков
//6)удалить из друзей означает удалить source_user из подписчиков target_user
    private final UserService userService;
    //изначально я планировал оставить все в userService, но для разделения логики вынес
    //подписки и дружбу, но по факту это тот же userService, поэтому приходится обращаться к репозиторию
    //для сохранения объекта User например
    private final UserRepository userRepository;

    public List<UserResponse> converterListUserToUserResponse(List<User> listToOperate) {
        return listToOperate.stream().map(sub->
                new UserResponse(sub.getId(), sub.getName(), sub.getMail())).toList();
    }


    public ResponseEntity<?> sendFriendRequestByUserId(Long targetUserId) {
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //здесь не нужно проверять optional,тк перед попаданием в контекст
        //source юзера ищут в базе
        User sourceUser = userService.findByUsername(contextUserName).get();
        //а вот targetUser нужно проверить, тк такого id может не быть
        //здесь нельзя применить метод userService getUserById, тк возвращается dto
        //будем использовать findById
        Optional<User> optionalTargetUser = userService.findById(targetUserId);
        if(optionalTargetUser.isPresent()) {
            User targetUser = optionalTargetUser.get();

            if(targetUser.getSubscribers().contains(sourceUser)){//если source user уже подписан на target_user
                return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                        String.format("Юзер с id %d уже подписан на id %d", sourceUser.getId(),
                                targetUserId)),
                        HttpStatus.BAD_REQUEST);
            } else {
                sourceUser.addSubscriptionToUser(targetUser);
                targetUser.addSubscriberToUser(sourceUser);
//            userRepository.save(targetUser); достаточно сохранить одного юзера
                userRepository.save(sourceUser);
                return ResponseEntity.ok(String.format("Юзер с id %d теперь подписан на id %d", sourceUser.getId(),
                        targetUserId));
            }

        } else {
            return new ResponseEntity<>(String.format("Попытка подписаться на/подружиться с несуществующим юзером id %d ",
                    targetUserId), HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<?> unsubscribeByUserId(Long targetUserId) {
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User sourceUser = userService.findByUsername(contextUserName).get();
        Optional<User> optionalTargetUser = userService.findById(targetUserId);
        if(optionalTargetUser.isPresent()) {
            User targetUser = optionalTargetUser.get();
            //cначала проверим подписан ли вообще
            if(targetUser.getSubscribers().contains(sourceUser)) {
                    targetUser.getSubscribers().remove(sourceUser);
                    sourceUser.getSubscriptions().remove(targetUser);
                    userRepository.save(sourceUser);//достаточно сохранить одного, но убрать нужно у обоих.

                return ResponseEntity.ok(String.format("Юзер с id %d теперь не подписан на id %d", sourceUser.getId(),
                        targetUserId));
            } else {
                return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                        String.format("Нет подписки на юзера с id %d ",
                                targetUserId)),
                        HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    String.format("Попытка отписаться от несуществующего юзера с id %d ",
                            targetUserId)),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getCurrentUserActiveFriendRequests() {
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User sourceUser = userService.findByUsername(contextUserName).get();

        List<User> subscriptions = sourceUser.getSubscriptions();
        List<User> subscribers = sourceUser.getSubscribers();
        subscribers.removeAll(subscriptions);//subscribers->activeFriendRequests
//        List<UserResponse> userResponses = subscribers.stream().map(sub->
//                new UserResponse(sub.getId(), sub.getName(), sub.getMail())).toList();
        return ResponseEntity.ok(converterListUserToUserResponse(subscribers));

    }

    ///cкорее всего не совсем рационально писать отдельный метод
    //под проверку личных подписок,хотя это некоторое разделение логики
    public ResponseEntity<?> getCurrentUserSubscriptions() {
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //здесь не нужно проверять optional,тк перед попаданием в контекст
        //source юзера ищут в базе
        User sourceUser = userService.findByUsername(contextUserName).get();
        //нужно отмаппить список юзеров-подписок в список dto userResponse
        List<User> subscriptions = sourceUser.getSubscriptions();
//        List<UserResponse> userResponses = subscriptions.stream().map(sub->
//                new UserResponse(sub.getId(), sub.getName(), sub.getMail())).toList();
        return ResponseEntity.ok(converterListUserToUserResponse(subscriptions));

    }

    public ResponseEntity<?> getCurrentUserSubscribers() {
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User sourceUser = userService.findByUsername(contextUserName).get();

        List<User> subscribers = sourceUser.getSubscribers();
//        List<UserResponse> userResponses = subscribers.stream().map(sub->
//                new UserResponse(sub.getId(), sub.getName(), sub.getMail())).toList();
        return ResponseEntity.ok(converterListUserToUserResponse(subscribers));
    }

    public ResponseEntity<?> getUserSubscribersByUserId(Long targetUserId) {
        //здесь нельзя применить метод userService getUserById, тк возвращается dto
        //будем использовать findById
        Optional<User> optionalTargetUser = userService.findById(targetUserId);
        if(optionalTargetUser.isPresent()) {
            User targetUser = optionalTargetUser.get();
            //нужно отмаппить список юзеров-подписчиков в список dto userResponse
            List<User> subscribers = targetUser.getSubscribers();
//            List<UserResponse> userResponses = subscribers.stream().map(sub->
//                    new UserResponse(sub.getId(), sub.getName(), sub.getMail())).toList();
            return ResponseEntity.ok(converterListUserToUserResponse(subscribers));

        } else {

            return  new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    String.format("Попытка получения списка подписок несуществующего юзера с id %d ",
                            targetUserId)),
                    HttpStatus.NOT_FOUND);
        }
    }



}
