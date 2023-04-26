package pl.allegrov2.allegrov2.validation.exceptions;

import pl.allegrov2.allegrov2.data.entities.cart.Cart;

public class EmptyCartException extends RuntimeException {

    public EmptyCartException(String message) {
        super(message);
    }

    public EmptyCartException() {
        super("Cart is empty");
    }

    public EmptyCartException(Cart cart){
        super("Cart with id " + cart.getId() + " is empty");
    }
}
