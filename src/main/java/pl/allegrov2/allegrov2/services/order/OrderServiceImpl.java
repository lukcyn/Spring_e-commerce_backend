package pl.allegrov2.allegrov2.services.order;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.data.entities.cart.Cart;
import pl.allegrov2.allegrov2.data.entities.cart.CartItem;
import pl.allegrov2.allegrov2.data.entities.order.Order;
import pl.allegrov2.allegrov2.repositories.OrderRepository;
import pl.allegrov2.allegrov2.services.cart.CartService;
import pl.allegrov2.allegrov2.services.product.ProductService;
import pl.allegrov2.allegrov2.services.user.UserService;
import pl.allegrov2.allegrov2.validation.exceptions.EmptyCartException;
import pl.allegrov2.allegrov2.validation.exceptions.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserService userService;
    private final CartService cartService;
    private final ProductService productService;

    private final OrderRepository orderRepository;

    @Override
    public Order createOrderForCart(String username) {
        AppUser user = userService.getUser(username);
        return createOrderAndApplyChanges(user.getCart(), user);
    }

    @Override
    public Order createOrderForCart(AppUser user) {
        Cart cart = user.getCart();
        checkIfCartValid(cart);
        return createOrderAndApplyChanges(cart, user);
    }

    @Override
    public Order getUserOrderById(String username, long id) {
        long userId = userService.getIdByUsername(username);

        return orderRepository.findByIdAndUserId(userId, id)
                .orElseThrow(() -> new NotFoundException("No order with id " + id + " for user " + username));
    }

    @Override
    public List<Order> getUserOrders(String username) {
        Long userId = userService.getIdByUsername(username);
        return orderRepository.findByUserId(userId);
    }

    private Map<Product, Integer> getNotAvailableItems(Cart cart){
        return cart.getCartItems().stream()
                .filter(item -> item.getQuantity() > item.getProduct().getStock())
                .collect(
                        Collectors.toMap(
                                CartItem::getProduct,
                                CartItem::getQuantity
                        )
                );
    }

    // TODO should be in cart service
    private void decreaseQuantityOfProducts(Cart cart){
        Map<Product, Integer> cartProducts = cart.getCartItems().stream()
                .collect(Collectors.toMap(CartItem::getProduct, CartItem::getQuantity));

        productService.decreaseQuantity(cartProducts);
    }

    private void checkIfCartValid(Cart cart){
        if(cart.getCartItems().isEmpty())
            throw new EmptyCartException();
//        Map<Product, Integer> notAvailableItems = getNotAvailableItems(cart);
//
//        if(!notAvailableItems.isEmpty())
//            throw new QuantityCriteriaNotMetException(notAvailableItems);
    }

    private Order createOrderAndApplyChanges(Cart cart, AppUser user){
        Order order = new Order(cart, user);

        decreaseQuantityOfProducts(cart);
        cartService.clearCart(cart);

        return orderRepository.save(order);
    }
}
