package com.jomik.apparel.interfaces.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.NoResultException;
import java.util.NoSuchElementException;

/**
 * Created by Mick on 4/16/2016.
 */
@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(
            {
                    NoResultException.class,
                    NoSuchElementException.class
            }
    )
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse notFoundHandler(Exception e){
        return new ErrorResponse(HttpStatus.NOT_FOUND, "Whatever you are looking for does not exist");
    }
}
