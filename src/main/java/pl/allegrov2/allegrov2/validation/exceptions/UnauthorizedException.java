package pl.allegrov2.allegrov2.validation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(){
        super();
    }
    public UnauthorizedException(String message){
        super(message);
    }
}
