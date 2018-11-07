package controllers;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@ControllerAdvice(("controllers"))
public class GlobalExceptionHandler {
    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({SQLException.class, NullPointerException.class, HibernateException.class})
    public String handleSQLException(HttpServletRequest request, Exception ex) {
        logger.error("Request occured:: " + request.getRequestURL(), ex);
        return "database-error";
    }
}
