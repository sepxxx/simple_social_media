package com.simple_social_media.services;


import com.simple_social_media.dtos.responses.PostResponse;
import com.simple_social_media.entities.User;
import com.simple_social_media.exceptions.AppError;
import com.simple_social_media.repositories.PostRepository;
import com.simple_social_media.entities.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

//    public List<Post> getAllPosts() {
//        return postRepository.findAll();
//    }


    public ResponseEntity<?> savePost(Post post) {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(null != securityContext.getAuthentication()){
            String contextUserName = (String) securityContext.getAuthentication().getPrincipal();
            User dbUser = userService.findByUsername(contextUserName).get();

            //одна из записей ниже обязательно заполнит таблицу user_post
            //не понятно нужно ли выполнять обе, но возникает ошибка
            //            dbUser.addPostToUser(post);
            post.setUser(dbUser);
            Post postWithId =  postRepository.save(post);
            return new ResponseEntity<>(new PostResponse(postWithId.getId(), postWithId.getHeader(),
                    postWithId.getText(), postWithId.getDate(), postWithId.getUser().getName(),
                    postWithId.getImage_url()), HttpStatus.CREATED);
        }
        //стоит обработать ошибку по authentication
        //но непонятно как на данном этапе она может быть пустой
        return new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "securityContext.getAuthentication()=null, невозможно установить владельца поста"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public ResponseEntity<?> getPost(Long id) {
        Optional<Post> optional = postRepository.findById(id);
        if(optional.isPresent()) {
            Post post = optional.get();
            return ResponseEntity.ok(new PostResponse(post.getId(), post.getHeader(),
                    post.getText(), post.getDate(), post.getUser().getName(),
                    post.getImage_url()));
        }
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                String.format("поста с id %d не существует", id)),
                HttpStatus.NOT_FOUND);
    }


    public ResponseEntity<?> deletePost(Long id) {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(null != securityContext.getAuthentication()){
            String contextUserName = (String) securityContext.getAuthentication().getPrincipal();

            //нужно переиспользовать метод из сервиса, чтобы не дублировать код
            //но не знаю можно ли вообще так писать, тк возвращается ResponseEntity
            ResponseEntity<?> responseEntity = getPost(id);
            if(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK)) {//если нашли пост, можно попробовать удалить
                PostResponse postResponse = (PostResponse)getPost(id).getBody();
                if (contextUserName.equals(postResponse.getUsernameCreatedBy())) {
                    postRepository.deleteById(id);
                    return ResponseEntity.ok(String.format("пост с id %d удален", id));
                } else {
                    return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(),
                            "удаление запрашивает не владелец поста"),
                            HttpStatus.FORBIDDEN);
                }
            } else {
                return responseEntity;//иначе возвращаем что такого поста нет
            }

        } else {
            //стоит обработать ошибку по authentication
            //но непонятно как на данном этапе она может быть пустой
            return new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "securityContext.getAuthentication()=null, невозможно установить владельца поста"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
