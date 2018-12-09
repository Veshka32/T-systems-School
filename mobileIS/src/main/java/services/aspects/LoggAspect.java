
/**
 * Apply aspects for classes methods in order to log some events;
 */

package services.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import services.implementations.TelegramBot;
import services.implementations.UserService;

import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
public class LoggAspect {
    /**
     * log create-update-delete operation in services classes
     */
    @AfterReturning(value = "execution(* services.implementations.*.create(..)) " +
            "|| execution(* services.implementations.*.update(..)) " +
            "|| execution(* services.implementations.*.delete(..))", returning = "returnValue")
    public void servicesCrudLogging(JoinPoint point, Object returnValue) {
        String method = point.getSignature().getName();
        Class cl = point.getTarget().getClass();
        String args = Arrays.toString(point.getArgs());
        Logger.getLogger(cl).info(method + " with args " + args + " was excecuted, returns " + returnValue);
    }

    /**
     * log about new user creation if target method is completed without errors
     */
    @AfterReturning(value = "execution(* services.implementations.UserService.*(..))", returning = "returnValue")
    public void userCreateLog(Object returnValue) {
        if (!((Optional<String>) returnValue).isPresent())
            Logger.getLogger(UserService.class).info("User created");
    }

    /**
     * log initialization configuration beans
     *
     */
    @After(value = "execution(* config.*.*(..)) && @annotation(org.springframework.context.annotation.Bean)")
    public void configBeanLogging(JoinPoint point) {
        String bean = point.getSignature().getName();
        Logger.getLogger(point.getTarget().getClass()).info("Initialized config bean: " + bean);
    }

    /**
     * log configuration beans' methods execution
     */
    @After("execution( void *config.*.*(..))")
    public void configVoidLogging(JoinPoint point) {
        Logger.getLogger(point.getTarget().getClass()).info("Execute config method: " + point.getSignature().getName());
    }

    /**
     * Log missed properties for telegramBot
     */
    @AfterThrowing(value = "execution(void *services.implementations.TelegramBot.sendMsg(..))", throwing = "ex")
    public void method2(NullPointerException ex) {
        Logger.getLogger(TelegramBot.class).warn("It seems as environment property for telegramBot are missed", ex);
    }
}
