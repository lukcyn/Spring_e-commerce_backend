package pl.allegrov2.allegrov2.validation.advices;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class MethodArgumentNotValidResolver {

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<String> notFoundHandler(MethodArgumentNotValidException ex){
        BindingResult result = ex.getBindingResult();
        List<FieldError> errors = result.getFieldErrors();

        return errors.stream().map(FieldError::getDefaultMessage).toList();
    }
}