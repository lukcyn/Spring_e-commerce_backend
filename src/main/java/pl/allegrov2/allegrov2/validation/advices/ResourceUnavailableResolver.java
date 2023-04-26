package pl.allegrov2.allegrov2.validation.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.allegrov2.allegrov2.validation.exceptions.ResourceUnavailableException;

@ControllerAdvice
public class ResourceUnavailableResolver {

    @ResponseBody
    @ExceptionHandler(ResourceUnavailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String unauthorizedHandler(ResourceUnavailableException ex){
        return ex.getMessage();
    }
}
