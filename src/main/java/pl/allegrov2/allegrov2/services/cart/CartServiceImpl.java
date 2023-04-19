package pl.allegrov2.allegrov2.services.cart;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.data.entities.cart.Cart;
import pl.allegrov2.allegrov2.data.entities.cart.CartItem;
import pl.allegrov2.allegrov2.repositories.CartRepository;
import pl.allegrov2.allegrov2.services.product.ProductService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    @Override
    public void addToCart(String userEmail, Long productId, int quantity) {
        Product product = productService.getById(productId);
        Cart cart = getCart(userEmail);

        addItemToCart(cart, product, quantity);
    }

    @Override
    public void addToCart(Long userId, Long productId, int quantity) {
        Product product = productService.getById(productId);
        Cart cart = getCart(userId);

        addItemToCart(cart, product, quantity);
    }

    @Override
    public Cart getCart(String userEmail) {
        return cartRepository.getCartOfUser(userEmail);
    }

    @Override
    public Cart getCart(Long userId) {
        return cartRepository.getCartOfUser(userId);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Cart removeItem(String userEmail, Long productId, int decQuantity) {
        Cart cart = getCart(userEmail);
        Product product = productService.getById(productId);    // TODO find in cart?
        Optional<CartItem> cartItem = getItemIfPresent(cart, product);

        if(cartItem.isPresent())
            modifyItemQuantity(cart, cartItem.get(), product, decQuantity);
        else
            throw new IllegalArgumentException("No such product with id " + productId + " in cart");
        // TODO custom exception

        return cart;
    }

    public void modifyItemQuantity(Cart cart, CartItem item, Product product, int quantity){
        item.decreaseQuantity(quantity);

        if(item.getQuantity() == 0){
            cart.getCartItems().remove(item);
            cartRepository.deleteCartItem(cart.getId(), product.getId());
        }
        else
            cartRepository.save(cart);
    }

    private Optional<CartItem> getItemIfPresent(Cart cart, Product product) {
        return cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId()))
                .findFirst();
    }

    private void addItemToCart(Cart cart, Product product, int desiredQuantity) {

        long availableProducts = product.getStock();

        if (availableProducts - desiredQuantity < 0) {
            throw new IllegalArgumentException("Not enough products in stock"); // TODO create custom exception
        }

        // Check if there is an card item with product in cart
        Optional<CartItem> cartItemExisting = getItemIfPresent(cart, product);

        // Increase desiredQuantity
        if (cartItemExisting.isPresent()) {

            if(availableProducts - cartItemExisting.get().getQuantity() - desiredQuantity < 0){
                throw new IllegalArgumentException("Not enough products in stock"); // TODO create custom exception
            }
            cartItemExisting.get().addQuantity(desiredQuantity);

        } else {
            CartItem newCartItem = new CartItem(cart, product, desiredQuantity);
            cart.addItem(newCartItem);     // TODO test if in cartItem i need to set up reference to cart
        }

        cartRepository.save(cart);
    }

    private void addQuantityToCartItem(CartItem cartItem, long availableQuantity, int desiredQuantity){
        if(availableQuantity - cartItem.getQuantity() - desiredQuantity < 0){
            throw new IllegalArgumentException("Not enough products in stock"); // TODO create custom exception
        }
        cartItem.addQuantity(desiredQuantity);
    }

    private void addNewCartItem(Cart cart, Product product, int desiredQuantity){
        CartItem newCartItem = new CartItem(cart, product, desiredQuantity);
        cart.addItem(newCartItem);     // TODO test if in cartItem i need to set up reference to cart
    }
}
