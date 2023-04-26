package pl.allegrov2.allegrov2.services.cart;


import jakarta.transaction.Transactional;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.data.entities.cart.Cart;

public interface CartService {

    void addToCart(String userEmail, Long productId, int quantity);

    void addToCart(Long userId, Long productId, int quantity);

    void addToCart(Cart cart, Product product, int quantity);

    Cart getCart(String userEmail);

    Cart getCart(Long userId);


    /**
     * Modifies quantity of product in cart. If quantity after
     * change == 0, then removes the item from cart.
     * */
    @Transactional
    Cart removeItem(String userEmail, Long productId, int decQuantity);

    @Transactional
    Cart removeItem(Cart cart, Product product, int decQuantity);

    @Transactional
    void clearCart(Cart cart);
}
