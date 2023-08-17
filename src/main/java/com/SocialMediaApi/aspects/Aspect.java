package com.SocialMediaApi.aspects;


import com.SocialMediaApi.exceptions.AppError;
import com.SocialMediaApi.services.PostService;
import com.SocialMediaApi.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Slf4j
@Component
@org.aspectj.lang.annotation.Aspect
@RequiredArgsConstructor
public class Aspect {
    private final PostService postService;
    private final UserService userService;


    @Pointcut("execution(* com.SocialMediaApi.services.FriendsAndSubsService.getCurrentUser*(..))")
    public void friendsAndSubsServiceCurrentUserMethodsPointcut() {}
    @Pointcut("execution(* com.SocialMediaApi.services.UserService.getAllUsers(..))")
    public void userServiceGetAllUsersPointcut() {}
    @Pointcut("execution(* com.SocialMediaApi.services.UserService.deleteUserById(..))")
    public void userServiceDeleteUserByIdPointcut() {}
    @Pointcut("execution(* com.SocialMediaApi.services.PostService.*Current*(..))")
    public void postServiceCurrentUserMethodsPointcut() {}

    @Around("friendsAndSubsServiceCurrentUserMethodsPointcut() || userServiceGetAllUsersPointcut()" +
            " || userServiceDeleteUserByIdPointcut() || postServiceCurrentUserMethodsPointcut()")
    public Object aroundCurrentUserMethodsAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
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



    //getUserSubscribers/Subscriptions/FriendsByUserId + subscribe/unsubscribeByUserId
    @Pointcut("execution(* com.SocialMediaApi.services.FriendsAndSubsService.*(Long))")
    public void friendsAndSubsServiceUserByIdMethodsPointcut(){}

    @Around("friendsAndSubsServiceUserByIdMethodsPointcut()")
    public Object aroundUserByIdMethodsAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
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
