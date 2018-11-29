package controllers;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(("controllers"))
public class GlobalExceptionHandler {
    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({RuntimeException.class})
    public String handleException(HttpServletRequest request, Exception ex) {
        logger.error("Request occurred:: " + request.getRequestURL(), ex);
        return "error";
    }
}
