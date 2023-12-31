package com.SocialMediaApi.services;

import com.SocialMediaApi.dtos.responses.UserResponse;
import com.SocialMediaApi.entities.User;
import com.SocialMediaApi.exceptions.AppError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FriendsAndSubsService {
    //1)Отправка запроса дружбы по факту представляет собой добавление target_user в список своих подписок
//+ добавление source_user в список подписчиков target_user
//2)Отмена подписки означает удаление source_user из подписчиков target_user, target_user из подписок source_user
//3)Проверка активных заявок означает возврат списка подписчиков минус подписок
//4)Принять запрос дружбы означает подписаться в ответ
//5)проверка списка друзей означает возврат пересечения списков подписок и подписчиков
//6)удалить из друзей означает отписаться - удалить source_user из подписчиков target_user
    private final UserService userService;
    public List<UserResponse> converterListUserToUserResponse(List<User> listToOperate) {
        return listToOperate.stream().map(sub->
                new UserResponse(sub.getId(), sub.getUsername(), sub.getEmail())).toList();
    }

    public List<User> makeUserFriendsList(User user) {
        List<User> subscriptions = user.getSubscriptions();
        List<User> subscribers = user.getSubscribers();
        //для формирования списка друзей нужно пересечь списки подписчиков и подписок
        subscribers.retainAll(subscriptions);
        return subscribers;
    }

    //В 5 методах ниже нельзя применить метод userService getUserById, тк возвращается dto
    //будем использовать findById
    //так же проводится проверка существования юзера в advice
    public ResponseEntity<?> getUserSubscribersByUserId(Long targetUserId) {
        User targetUser = userService.findById(targetUserId).get();
        return ResponseEntity.ok(converterListUserToUserResponse(
                targetUser.getSubscribers()
        ));

    }
    public ResponseEntity<?> getUserSubscriptionsByUserId(Long targetUserId) {
        User targetUser = userService.findById(targetUserId).get();
        return ResponseEntity.ok(converterListUserToUserResponse(
                targetUser.getSubscriptions()
        ));
    }
    public ResponseEntity<?> getUserFriendsByUserId(Long targetUserId) {
        User targetUser = userService.findById(targetUserId).get();
        return ResponseEntity.ok(converterListUserToUserResponse(
                makeUserFriendsList(targetUser)
        ));

    }
    public ResponseEntity<?> subscribeByUserId(Long targetUserId) {
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //не нужно проверять optional,перед попаданием в контекст source ищут в базе
        User sourceUser = userService.findByUsername(contextUserName).get();
        User targetUser = userService.findById(targetUserId).get();
        if(!Objects.equals(sourceUser.getId(), targetUserId)) {
            if (targetUser.getSubscribers().contains(sourceUser)) {//если source user уже подписан на target_user
                return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                        String.format("Юзер с id %d уже подписан на id %d", sourceUser.getId(),
                                targetUserId)),
                        HttpStatus.BAD_REQUEST);
            } else {
                sourceUser.addSubscriptionToUser(targetUser);
                targetUser.addSubscriberToUser(sourceUser);
//            userRepository.save(targetUser); достаточно сохранить одного юзера
//                userRepository.save(sourceUser);
                userService.saveUserByEntity(sourceUser);
                return ResponseEntity.ok(String.format("Юзер с id %d теперь подписан на id %d", sourceUser.getId(),
                        targetUserId));
            }
        } else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),"Нельзя подписаться на себя") ,
                    HttpStatus.BAD_REQUEST);
        }

    }
    public ResponseEntity<?> unsubscribeByUserId(Long targetUserId) {
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //не нужно проверять optional,перед попаданием в контекст source ищут в базе
        User sourceUser = userService.findByUsername(contextUserName).get();
        User targetUser = userService.findById(targetUserId).get();
        //cначала проверим подписан ли вообще
        if(targetUser.getSubscribers().contains(sourceUser)) {
                targetUser.getSubscribers().remove(sourceUser);
                sourceUser.getSubscriptions().remove(targetUser);
//                userRepository.save(sourceUser);//достаточно сохранить одного, но убрать нужно у обоих.
                  userService.saveUserByEntity(sourceUser);
            return ResponseEntity.ok(String.format("Юзер с id %d теперь не подписан на id %d", sourceUser.getId(),
                    targetUserId));
        } else {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    String.format("Нет подписки на юзера с id %d ",
                            targetUserId)),
                    HttpStatus.BAD_REQUEST);
        }

    }
    public ResponseEntity<?> getUserFriendRequests() {
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User sourceUser = userService.findByUsername(contextUserName).get();
        List<User> subscriptions = sourceUser.getSubscriptions();
        List<User> subscribers = sourceUser.getSubscribers();
        subscribers.removeAll(subscriptions);//subscribers->activeFriendRequests
        return ResponseEntity.ok(converterListUserToUserResponse(subscribers));

    }

    ///cкорее всего не совсем рационально писать отдельный метод для текущего юзера
    //под проверку личных списков
    //N:проверка контекста проводится в адвайсе
//    public ResponseEntity<?> getCurrentUserSubscriptions() {
//        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        //не проверять optional,тк перед попаданием в контекст source юзера ищут в базе
//        User sourceUser = userService.findByUsername(contextUserName).get();
//        //нужно отмаппить список юзеров-подписок в список dto userResponse
//        return ResponseEntity.ok(converterListUserToUserResponse(
//                sourceUser.getSubscriptions()));
//
//    }
//
//    public ResponseEntity<?> getCurrentUserSubscribers() {
//        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User sourceUser = userService.findByUsername(contextUserName).get();
//        return ResponseEntity.ok(converterListUserToUserResponse(
//                sourceUser.getSubscribers()));
//    }
//
//    public ResponseEntity<?> getCurrentUserFriends() {
//        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User sourceUser = userService.findByUsername(contextUserName).get();
//        return ResponseEntity.ok(converterListUserToUserResponse(
//                makeUserFriendsList(sourceUser)));
//    }







}
