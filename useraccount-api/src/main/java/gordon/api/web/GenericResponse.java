package gordon.api.web;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

public class GenericResponse {
    private String message;
    private String error;

    public GenericResponse() {
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public GenericResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public GenericResponse setError(String error){
        this.error = error;
        return this;
    }

    public GenericResponse setMessage(List<ObjectError> validationErrors) {
        String temp = validationErrors.stream().map(e -> {
           if (e instanceof FieldError){
               return "{\"field\":\"" + ((FieldError) e).getField() + "\",\"defaultMessage\":\"" + e.getDefaultMessage() + "\"}";
           } else {
               return "{\"object\":\"" + e.getObjectName() + "\",\"defaultMessage\":\"" + e.getDefaultMessage() + "\"}";
           }
        }).collect(Collectors.joining(","));
        this.message = "["+temp+"]";
        return this;
    }
}
