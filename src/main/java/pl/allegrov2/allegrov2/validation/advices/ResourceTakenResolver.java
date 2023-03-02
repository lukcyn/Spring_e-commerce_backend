package pl.allegrov2.allegrov2.validation.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.allegrov2.allegrov2.validation.exceptions.ResourceTakenException;

@ControllerAdvice
public class ResourceTakenResolver {

    @ResponseBody
    @ExceptionHandler(ResourceTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String resourceTakenHandler(ResourceTakenException ex){
        return ex.getMessage();
    }
}
