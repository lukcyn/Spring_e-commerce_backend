package pl.allegrov2.allegrov2.validation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class MismatchException extends RuntimeException {

    public MismatchException(){
        super();
    }

    public MismatchException(String message){
        super(message);
    }
}
