package pl.allegrov2.allegrov2.services.cart;


import pl.allegrov2.allegrov2.data.entities.cart.Cart;

public interface CartService {

    void addToCart(String userEmail, Long productId, int quantity);

    void addToCart(Long userId, Long productId, int quantity);

    Cart getCart(String userEmail);

    Cart getCart(Long userId);

    Cart removeItem(String userEmail, Long productId, int decQuantity);
}
