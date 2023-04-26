package pl.allegrov2.allegrov2.validation.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.allegrov2.allegrov2.validation.exceptions.EmptyCartException;

@ControllerAdvice
public class EmptyCartResolver {

    @ResponseBody
    @ExceptionHandler(EmptyCartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleExpiredJwtException(EmptyCartException e) {
        return e.getMessage();
    }
}
