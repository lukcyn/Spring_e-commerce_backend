package pl.allegrov2.allegrov2.services.cart;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.data.entities.cart.Cart;
import pl.allegrov2.allegrov2.data.entities.cart.CartItem;
import pl.allegrov2.allegrov2.repositories.CartItemRepository;
import pl.allegrov2.allegrov2.repositories.CartRepository;
import pl.allegrov2.allegrov2.services.product.ProductService;
import pl.allegrov2.allegrov2.validation.exceptions.NotFoundException;
import pl.allegrov2.allegrov2.validation.exceptions.ResourceUnavailableException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CartItemRepository cartItemRepository;

    @Override
    public void addToCart(String userEmail, Long productId, int quantity) {
        Product product = productService.getById(productId);
        Cart cart = getCart(userEmail);

        addToCart(cart, product, quantity);
    }

    @Override
    public void addToCart(Long userId, Long productId, int quantity) {
        Product product = productService.getById(productId);
        Cart cart = getCart(userId);

        addToCart(cart, product, quantity);
    }

    @Override
    public void addToCart(Cart cart, Product product, int quantity) {
        long availableProducts = product.getStock();
        throwIfUnavailable(availableProducts, quantity);

        Optional<CartItem> cartItemExisting = getItemIfPresent(cart, product);

        if (cartItemExisting.isPresent()) {
            CartItem item = cartItemExisting.get();
            throwIfUnavailable(availableProducts - item.getQuantity(), quantity);
            item.addQuantity(quantity);
        } else {
            CartItem newCartItem = new CartItem(cart, product, quantity);
            cart.addItem(newCartItem);
        }

        cartRepository.save(cart);
    }

    @Override
    public Cart getCart(String userEmail) {
        return cartRepository.getCartOfUser(userEmail)
                .orElseThrow(() -> new NotFoundException("No cart for user with email " + userEmail));
    }

    @Override
    public Cart getCart(Long userId) {
        return cartRepository.getCartOfUser(userId)
                .orElseThrow(() -> new NotFoundException("No cart for user with id " + userId));
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Cart removeItem(String userEmail, Long productId, int decQuantity) {
        Cart cart = getCart(userEmail);
        Product product = productService.getById(productId);

        return removeItem(cart, product, decQuantity);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Cart removeItem(Cart cart, Product product, int decQuantity) {
        Optional<CartItem> cartItem = getItemIfPresent(cart, product);

        if(cartItem.isPresent())
            modifyCartItemQuantity(cart, cartItem.get(), product, decQuantity);
        else
            throw new NotFoundException("No such product with id " + product.getId() + " in cart");

        return cart;
    }

    @Override
    @Transactional
    public void clearCart(Cart cart) {
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    public void modifyCartItemQuantity(Cart cart, CartItem item, Product product, int quantity){
        item.decreaseQuantity(quantity);

        if(item.getQuantity() == 0){
            cart.getCartItems().remove(item);
            cartRepository.deleteCartItem(cart.getId(), product.getId());
        }
        else
            cartItemRepository.save(item);
    }

    private Optional<CartItem> getItemIfPresent(Cart cart, Product product) {
        return cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId()))
                .findFirst();
    }

    private void throwIfUnavailable(long availableQuantity, int desiredQuantity){
        if(availableQuantity - desiredQuantity < 0){
            throw new ResourceUnavailableException("Not enough products in stock. " +
                    "Required: " + desiredQuantity + "; Available: " + availableQuantity);
        }
    }
}
