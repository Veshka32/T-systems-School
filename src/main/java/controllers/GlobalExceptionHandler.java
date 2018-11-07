package controllers;

import config.WebMvcConfig;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice(("controllers"))
public class GlobalExceptionHandler {
    private static final Logger logger = Logger.getLogger(WebMvcConfig.class);

    @ExceptionHandler({SQLException.class, NullPointerException.class})
    public String handleSQLException(Exception ex) {
        logger.info(ex.getMessage());
        logger.trace(ex);
        return "redirect:/database_error";
    }
}
