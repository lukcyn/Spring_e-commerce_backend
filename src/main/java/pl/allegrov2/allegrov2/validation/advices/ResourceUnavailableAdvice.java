package pl.allegrov2.allegrov2.validation.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.allegrov2.allegrov2.validation.exceptions.ResourceUnavailableException;
import pl.allegrov2.allegrov2.validation.exceptions.UnauthorizedException;

@ControllerAdvice
public class ResourceUnavailableAdvice {

    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String unauthorizedHandler(ResourceUnavailableException ex){
        return ex.getMessage();
    }
}
