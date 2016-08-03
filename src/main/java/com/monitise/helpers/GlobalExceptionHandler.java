package com.monitise.helpers;

import com.monitise.models.BaseException;
import com.monitise.models.Error;
import com.monitise.models.Response;
import org.omg.CORBA.Object;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Response<Object> handleBaseException(BaseException exception) {
        Response<Object> response = new Response<>();
        Error error = new Error(exception.getCode(), exception.getMessage());
        response.setError(error);
        return response;
    }

}