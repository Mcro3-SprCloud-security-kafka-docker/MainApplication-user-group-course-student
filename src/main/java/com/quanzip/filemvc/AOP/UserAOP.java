package com.quanzip.filemvc.AOP;

import com.quanzip.filemvc.service.CacheService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

// We can use Aspect to write log when execute function
// or we can delete cache after update data
// or we can delele files produced after finishing function execution.

@Aspect
@Component
public class UserAOP {
    private final Logger logger = LoggerFactory.getLogger(UserAOP.class);

    public UserAOP(CacheManager cacheManager) {
//        use this object to call to APIs of other system
//        This is just for illustrating, not for any official purposes.
        RestTemplate restTemplate = new RestTemplate();
    }

    @Autowired
    private CacheService cacheService;

//  Very important: when function that we want to intercept has more than 1 argument: then function name in Execution expression
//    must have .. like below (...searchAndViewListUsers(..)), if there is one, it must be * instead.
    @Before(value = "execution(* com.quanzip.filemvc.controller.UserController.searchAndViewListUsers(..))")
    public void writeLogWhenSearch(JoinPoint joinPoint){
        logger.info("Execution before search for user...");

    }

//    Very important: the function deleteUser in UserController only have 1 arg, so when we want to intercept it here
//     we use * for it like below: deleteUser(*)
    @After(value = "execution(* com.quanzip.filemvc.controller.UserController.deleteUser(*))")
    public void writeLogWhenDelete(JoinPoint joinPoint){
        logger.info("Execution after search for user...");
        Object[] args = joinPoint.getArgs();
        System.out.println(Arrays.toString(args));
    }

    // we have
//    @AfterReturning
//    @AfterThrowing

    @After(value = "execution(* com.quanzip.filemvc.controller.UserController.searchAndViewListUsers(..))")
    public void writeLogWhenFinishSearch(){
        logger.info("Execution after search for user...");
    }
}
