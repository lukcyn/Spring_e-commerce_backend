package pl.allegrov2.allegrov2.services.cart;


import jakarta.transaction.Transactional;
import pl.allegrov2.allegrov2.data.entities.cart.Cart;

public interface CartService {

    void addToCart(String userEmail, Long productId, int quantity);

    void addToCart(Long userId, Long productId, int quantity);

    Cart getCart(String userEmail);

    Cart getCart(Long userId);

    @Transactional
    Cart removeItem(String userEmail, Long productId, int decQuantity);

    @Transactional
    void clearCart(Cart cart);
}
