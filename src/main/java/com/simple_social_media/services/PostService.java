package com.simple_social_media.services;


import com.simple_social_media.dtos.requests.PostRequest;
import com.simple_social_media.dtos.responses.PostResponse;
import com.simple_social_media.entities.User;
import com.simple_social_media.exceptions.AppError;
import com.simple_social_media.repositories.PostRepository;
import com.simple_social_media.entities.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
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
                    postWithId.getText(), postWithId.getDate(), postWithId.getUser().getUsername(),
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
                    post.getText(), post.getDate(), post.getUser().getUsername(),
                    post.getImage_url()));
        } else {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    String.format("поста с id %d не существует", id)),
                    HttpStatus.NOT_FOUND);
        }

    }


    public ResponseEntity<?> deletePost(Long id) {
                    postRepository.deleteById(id);
                    return ResponseEntity.ok(String.format("пост с id %d удален", id));
    }

    public ResponseEntity<?> updatePost(Long id, PostRequest pR) {
        //чтобы обновить пост нужно получить из бд пост по id
        //затем пересобрать заново
        //1) если будем использовать getPost из репозитория
        //для сборки понадобиться вытащить пользователя из бд
        //чтобы заново связать пост и юзера
        //2)если повторим немного код getPost, то не придется вытаскивать юзера
        //из бд, тк получим не dto поста, а сам Post

        //хотя не совсем понятно как отработает метод post.getUser() при проверке разрешения
        //будет ли обращение к бд в таком случае?
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(null != securityContext.getAuthentication()){
            String contextUserName = (String) securityContext.getAuthentication().getPrincipal();

            Optional<Post> optional = postRepository.findById(id);
            if(optional.isPresent()) {


                Post post = optional.get();

                if(contextUserName.equals(post.getUser().getUsername())) {//если в контексте и посте юзеры одинаковы
                    post.setText(pR.getText());
                    post.setHeader(pR.getHeader());
                    post.setDate(new Date());
                    post.setImage_url(pR.getImage_url());
                    //id и user'a менять не нужно
                    postRepository.save(post);
                    return ResponseEntity.ok(new PostResponse(post.getId(), post.getHeader(),
                            post.getText(), post.getDate(), post.getUser().getUsername(),
                            post.getImage_url()));
                } else {
                    return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(),
                            "обновление запрашивает не владелец поста"),
                            HttpStatus.FORBIDDEN);
                }



            } else {
                return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                        String.format("пост с id %d не существует", id)),
                        HttpStatus.NOT_FOUND);
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
