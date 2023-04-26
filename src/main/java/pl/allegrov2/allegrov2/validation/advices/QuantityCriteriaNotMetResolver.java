package pl.allegrov2.allegrov2.validation.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.allegrov2.allegrov2.validation.exceptions.QuantityCriteriaNotMetException;


@ControllerAdvice
public class QuantityCriteriaNotMetResolver {

    @ResponseBody
    @ExceptionHandler(QuantityCriteriaNotMetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> productQuantityCriteriaNotMetHandler(QuantityCriteriaNotMetException ex){
        return new ResponseEntity<>(ex.getDetailedMessage(), HttpStatus.BAD_REQUEST);
    }
}
