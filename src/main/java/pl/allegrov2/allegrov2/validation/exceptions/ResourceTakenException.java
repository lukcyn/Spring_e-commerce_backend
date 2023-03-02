package pl.allegrov2.allegrov2.validation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceTakenException extends RuntimeException {

    public ResourceTakenException(){
        super();
    }

    public ResourceTakenException(String message){
        super(message);
    }
}
