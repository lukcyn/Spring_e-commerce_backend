package pl.allegrov2.allegrov2.validation.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.allegrov2.allegrov2.validation.exceptions.MismatchException;

@ControllerAdvice
public class MismatchResolver {

    @ResponseBody
    @ExceptionHandler(MismatchException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String notFoundHandler(MismatchException ex){
        return ex.getMessage();
    }
}
