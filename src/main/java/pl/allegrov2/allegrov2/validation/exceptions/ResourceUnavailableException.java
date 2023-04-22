package pl.allegrov2.allegrov2.validation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceUnavailableException extends RuntimeException {

    public ResourceUnavailableException(){
        super();
    }

    public ResourceUnavailableException(String message){
        super(message);
    }
}
