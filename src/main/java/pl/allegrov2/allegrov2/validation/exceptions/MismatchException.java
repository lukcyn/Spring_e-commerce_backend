package pl.allegrov2.allegrov2.validation.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MismatchException extends RuntimeException {

    public MismatchException(String message){
        super(message);
    }
}
