package com.simple_social_media.services;

import com.simple_social_media.dtos.requests.ActivityFeedRequest;
import com.simple_social_media.dtos.responses.PostResponse;
import com.simple_social_media.entities.Post;
import com.simple_social_media.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityFeedService {
    private final UserService userService;
    private final FriendsAndSubsService friendsAndSubsService;
    //нужно отобразить последние посты подписок

    //получаем подписки юзера
    //у каждой подписки берем посты
    //берем последний
    //формируем список по времени поста
    //список нужно разбит для пагинации

    //пусть на странице 5 постов
    //size/5 страниц с 5 или менее постов

    //метод принимает текущую страницу и макс постов
    //вопросы:

    //2)оптимально ли каждый раз формировать ленту
    //3)если не формировать каждый раз, а хранить где-то
    // что если она изменилась во время запроса другой страницы

    public ResponseEntity<?> getCurrentUserActivityFeed(ActivityFeedRequest activityFeedRequest) {
        //C:проверка контекста
        Integer limit = activityFeedRequest.getLimit();
        Integer page = activityFeedRequest.getPage();
        if(page>0 &&  limit> 0) {
            String contextUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User contextUser = userService.findByUsername(contextUsername).get();
            List<User> friends = friendsAndSubsService.makeUserFriendsList(contextUser);
            List<PostResponse> postResponseList = friends.stream().map((u) -> {
                List<Post> posts = u.getPosts();
                Post lastPost = posts.get(posts.size() - 1);
                return new PostResponse(lastPost.getId(), lastPost.getHeader(), lastPost.getText(), lastPost.getDate()
                        , lastPost.getUser().getUsername(), lastPost.getImage_url());
            }).toList();
            postResponseList = postResponseList.stream().sorted(Comparator.comparing(PostResponse::getDate)).toList();
            //необходимо получить номера постов попадающих в текущую страницу
            Integer indexFrom = page*limit;
            //что если постов < 5
            //что если на последней странице 2 поста например
            return ResponseEntity.ok(postResponseList.subList(indexFrom, indexFrom+5));
        } else {
            return new ResponseEntity<>("некорректные значения номера страницы " +
                    "либо максимума постов на страницу", HttpStatus.BAD_REQUEST);
        }
    }
}
