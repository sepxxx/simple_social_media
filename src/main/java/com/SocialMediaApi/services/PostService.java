package com.SocialMediaApi.services;


import com.SocialMediaApi.dtos.requests.PostRequest;
import com.SocialMediaApi.dtos.responses.PostResponse;
import com.SocialMediaApi.entities.User;
import com.SocialMediaApi.exceptions.AppError;
import com.SocialMediaApi.repositories.PostRepository;
import com.SocialMediaApi.entities.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    private final RoleService roleService;
    public ResponseEntity<?> saveCurrentUserPost(PostRequest pR) {
        //C: проводится проверка контекста
        String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User dbUser = userService.findByUsername(contextUserName).get();
        Post post = new Post(pR.getHeader(), pR.getText(), pR.getImage_url());
        post.setUser(dbUser);
        Post postWithId =  postRepository.save(post);
        return new ResponseEntity<>(new PostResponse(postWithId.getId(), postWithId.getHeader(),
                postWithId.getText(), postWithId.getDate(), postWithId.getUser().getUsername(),
                postWithId.getImage_url()), HttpStatus.CREATED);

    }

    public ResponseEntity<?> deleteCurrentUserPost(Long id) {
        //C: проводится проверка контекста
        ResponseEntity<?> responseEntity = getPost(id);
        if(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK)) {//если нашли пост, можно попробовать удалить/изменить
            String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User contextUser = userService.findByUsername(contextUserName).get();
            PostResponse postResponse = (PostResponse)responseEntity.getBody();
            if (contextUserName.equals(postResponse.getUsernameCreatedBy()) ||
                    contextUser.getRoles().contains(roleService.getAdminRole())) {
                postRepository.deleteById(id);
                return ResponseEntity.ok(String.format("пост с id %d удален", id));
            } else {
                return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(),
                        "Запрашивает не владелец поста"),
                        HttpStatus.FORBIDDEN);
            }
        } else {
            return responseEntity;//иначе возвращаем что такого поста нет
        }
    }

    //чтобы обновить пост нужно получить из бд пост по id
    //затем пересобрать заново
    //1) если будем использовать getPost из репозитория
    //для сборки понадобиться вытащить пользователя из бд
    //чтобы заново связать пост и юзера
    //2)если повторим немного код getPost, то не придется вытаскивать юзера
    //из бд, тк получим не dto поста, а сам Post
    public ResponseEntity<?> updateCurrentUserPost(Long id, PostRequest pR) {
        //C: проводится проверка контекста
        Optional<Post> optional = postRepository.findById(id);
        if(optional.isPresent()) {
            Post post = optional.get();
            String contextUserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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


}
