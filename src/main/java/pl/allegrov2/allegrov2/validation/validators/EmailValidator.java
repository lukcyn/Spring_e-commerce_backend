package pl.allegrov2.allegrov2.validation.validators;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidator implements Predicate<String> {

    @Override
    public boolean test(String s) {
//        TODO: Regex to validate email
        return true;
    }
}
