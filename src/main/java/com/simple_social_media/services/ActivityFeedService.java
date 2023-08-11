package com.simple_social_media.services;

import com.simple_social_media.dtos.requests.ActivityFeedRequest;
import com.simple_social_media.dtos.responses.ActivityFeedResponse;
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

            System.out.println("!!!!! SUBSCRIPTION LIST SIZE " + contextUser.getSubscriptions().size());


            List<PostResponse> postResponseList = contextUser.getSubscriptions().stream().map((u) -> {
                List<Post> posts = u.getPosts();
                //у каждого юзера мы должны получить последний пост
                if(posts.size()>0) {
                    Post lastPost = posts.get(posts.size() - 1);
                    return new PostResponse(lastPost.getId(), lastPost.getHeader(), lastPost.getText(), lastPost.getDate()
                            , lastPost.getUser().getUsername(), lastPost.getImage_url());
                } else {
                    return null;
                }
            }).toList();
            System.out.println("!!!!! POSTRESPONSES LIST SIZE NOT SORTED " + postResponseList.size());

            postResponseList = postResponseList.stream().sorted(Comparator.comparing(PostResponse::getDate)).toList();

            int indexFrom = (page - 1) * limit;
            int lastInd = postResponseList.size() - 1;//поледний индекс нашего листа
            System.out.println("!!!!! LAST IND " + lastInd);
            //sublist не трогает последний, значит в прошлом запросе мы его не зацепили
            if (indexFrom > 0) indexFrom--;
            System.out.println("!!!!! INDEX FROM " + indexFrom);
            //определим общее число возможных страниц
            int pagesAmount = postResponseList.size() / limit;
            if(postResponseList.size() % limit!=0)
                pagesAmount++;//последняя страница будет неполной

            if (lastInd >= indexFrom + limit - 1){//постов больше чем нужно или =
                postResponseList = postResponseList.subList(indexFrom, indexFrom + limit);
                return ResponseEntity.ok(new ActivityFeedResponse(postResponseList, pagesAmount));
            } else if (lastInd > indexFrom) {//постов меньше чем нужно, но они есть
                postResponseList = postResponseList.subList(indexFrom, lastInd);
                return ResponseEntity.ok(new ActivityFeedResponse(postResponseList, pagesAmount));
            }else {
                return new ResponseEntity<>("такой страницы нет", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("limit,page должны быть > 0", HttpStatus.BAD_REQUEST);
        }
    }




}
