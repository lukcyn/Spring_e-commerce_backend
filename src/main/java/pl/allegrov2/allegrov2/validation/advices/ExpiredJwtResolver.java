package pl.allegrov2.allegrov2.validation.advices;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.allegrov2.allegrov2.controllers.AuthController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// fixme remove this
@ControllerAdvice
public class ExpiredJwtResolver {

    @ResponseBody
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleExpiredJwtException(ExpiredJwtException e) {
        return "Token has expired. Login at " +
                linkTo(methodOn(AuthController.class)
                .login(null))
                .withRel("Login");
    }
}
