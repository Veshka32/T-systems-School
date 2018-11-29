package services.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
public class LoggableAspect {

    @AfterReturning(value = "@annotation(Loggable)", returning = "returnValue")
    public void deleteLog1(JoinPoint point, Object returnValue) {
        String method = point.getSignature().toShortString();
        Class cl = point.getTarget().getClass();
        String args = Arrays.toString(point.getArgs());

        Optional<String> result = (Optional<String>) returnValue;
        Logger.getLogger(cl).info(method + " with args " + args + " was excecuted, returns " + result.toString());
    }
}
