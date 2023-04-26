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

import java.util.ArrayList;
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
        Product product = productService.getById(productId);
        Optional<CartItem> cartItem = getItemIfPresent(cart, product);

        if(cartItem.isPresent())
            modifyItemQuantity(cart, cartItem.get(), product, decQuantity);
        else
            throw new NotFoundException("No such product with id " + productId + " in cart");

        return cart;
    }

    // FIXME check if works
    @Override
    @Transactional
    public void clearCart(Cart cart) {
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
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
        throwIfUnavailable(availableProducts, desiredQuantity);

        Optional<CartItem> cartItemExisting = getItemIfPresent(cart, product);

        if (cartItemExisting.isPresent()) {
            CartItem item = cartItemExisting.get();
            throwIfUnavailable(availableProducts - item.getQuantity(), desiredQuantity);
            item.addQuantity(desiredQuantity);
        } else {
            CartItem newCartItem = new CartItem(cart, product, desiredQuantity);
            cart.addItem(newCartItem);
        }

        cartRepository.save(cart);
    }

    private void throwIfUnavailable(long availableQuantity, int desiredQuantity){
        if(availableQuantity - desiredQuantity < 0){
            throw new ResourceUnavailableException("Not enough products in stock. " +
                    "Required: " + desiredQuantity + "; Available: " + availableQuantity);
        }
    }
}
