package com.simple_social_media.aspects;


import com.simple_social_media.dtos.responses.PostResponse;
import com.simple_social_media.dtos.responses.UserResponse;
import com.simple_social_media.entities.User;
import com.simple_social_media.exceptions.AppError;
import com.simple_social_media.services.PostService;
import com.simple_social_media.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Slf4j
@Component
@org.aspectj.lang.annotation.Aspect
@RequiredArgsConstructor
public class Aspect {
    private final PostService postService;
    private final UserService userService;

//    @Around("execution(* com.simple_social_media.services.PostService.*(..))")
//    public Object aroundPostServiceMethodsAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
//        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
//        log.info("begin of " + methodSignature.getName());
//        Object targetMethodResult = proceedingJoinPoint.proceed();
//        log.info("end of" + methodSignature.getName());
//        return targetMethodResult;
//    }


    @Around("execution(* com.simple_social_media.services.PostService.deletePost(..))")
    public Object aroundPostServiceMethodsAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        List<Object> args =  Arrays.stream(proceedingJoinPoint.getArgs()).toList();
        //id всегда первый в аргументах
        Long id = (Long)args.get(0);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(null != securityContext.getAuthentication()){
            String contextUserName = (String) securityContext.getAuthentication().getPrincipal();

            //нужно переиспользовать метод из сервиса, чтобы не дублировать код
            //но не знаю можно ли вообще так писать, тк возвращается ResponseEntity
            ResponseEntity<?> responseEntity = postService.getPost(id);
            if(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK)) {//если нашли пост, можно попробовать удалить/изменить
                PostResponse postResponse = (PostResponse)responseEntity.getBody();
                if (contextUserName.equals(postResponse.getUsernameCreatedBy())) {
                    return proceedingJoinPoint.proceed();
                } else {
                    return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(),
                            "Запрашивает не владелец поста"),
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






    @Around("execution(* com.simple_social_media.services.FriendsAndSubsService.getCurrentUser*(..))")
    public Object aroundFriendsAndSubsServiceCurrentUserMethodsAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        if(null !=  SecurityContextHolder.getContext().getAuthentication()){
            return proceedingJoinPoint.proceed();
        } else {
            //стоит обработать ошибку по authentication
            //но непонятно как на данном этапе она может быть пустой
            return new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "securityContext.getAuthentication()=null"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //getUserSubscribers /Subcriptions/ Friends ByUserId
//    @Around("execution(* com.simple_social_media.services.FriendsAndSubsService.getUser*(..))")
    //getUserSubscribers/Subcriptions/FriendsByUserId + subscribe/unsubcribeByUserId
    @Around("execution(* com.simple_social_media.services.FriendsAndSubsService.*(Long))")
    public Object aroundFriendsAndSubsServiceUserByIdMethodsAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<Object> args =  Arrays.stream(proceedingJoinPoint.getArgs()).toList();
        //id всегда первый в аргументах
        Long id = (Long)args.get(0);
        if(userService.existsById(id)) {
            return proceedingJoinPoint.proceed();
        } else {
            return  new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    String.format("TargetUser с id %d не существует",
                            id)),
                    HttpStatus.NOT_FOUND);
        }

    }



}
