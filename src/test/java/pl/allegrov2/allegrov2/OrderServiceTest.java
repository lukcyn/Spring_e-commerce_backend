package pl.allegrov2.allegrov2;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.data.entities.cart.Cart;
import pl.allegrov2.allegrov2.data.entities.order.Order;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;
import pl.allegrov2.allegrov2.data.seeding.Seeder;
import pl.allegrov2.allegrov2.repositories.CartRepository;
import pl.allegrov2.allegrov2.repositories.ProductRepository;
import pl.allegrov2.allegrov2.repositories.UserRepository;
import pl.allegrov2.allegrov2.services.cart.CartService;
import pl.allegrov2.allegrov2.services.order.OrderService;
import pl.allegrov2.allegrov2.services.user.UserService;
import pl.allegrov2.allegrov2.validation.exceptions.EmptyCartException;
import pl.allegrov2.allegrov2.validation.exceptions.QuantityCriteriaNotMetException;
import pl.allegrov2.allegrov2.validation.exceptions.ResourceUnavailableException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(Transactional.TxType.NEVER)
public class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private Seeder seeder;

    private AppUser user;
    private Product product_1;
    private Product product_2;

    private final static int PRODUCT_1_QUANTITY = 5;
    private final static int PRODUCT_2_QUANTITY = 7;

    @BeforeEach
    public void init(){
        user = seeder.generateUser(AppUserRole.USER);
        product_1 = seeder.generateProduct(List.of("BRAND1", "BRAND2", "BRAND3"), PRODUCT_1_QUANTITY);
        product_2 = seeder.generateProduct(List.of("BRAND1", "BRAND2", "BRAND3"), PRODUCT_2_QUANTITY);

        userRepository.save(user);
        productRepository.save(product_1);
        productRepository.save(product_2);
    }

    @AfterEach
    public void cleanUp(){
        userRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void createOrderForCart_hasItems_itemsInStock(){
        int quantity = 1;
        long productStartQuantity = product_1.getStock();
        cartService.addToCart(user.getCart(), product_1, quantity);

        var order = orderService.createOrderForCart(user);

        // Order has one type of item
        assertEquals(1, order.getOrderItems().size());

        // Item has correct quantity
        assertEquals(quantity, order.getOrderItems().get(0).getQuantity());

        // Stock in database gets reduced
        assertEquals(productStartQuantity - quantity, order.getOrderItems().get(0).getProduct().getStock());

        // Items from cart get removed
        Cart dbCart = userService.getUser(user.getId()).getCart();
        assertTrue(user.getCart().getCartItems().isEmpty());
        assertTrue(dbCart.getCartItems().isEmpty());
    }

    @Test
    public void createOrderForCart_hasNoItems(){
        assertThrows(EmptyCartException.class,  () -> orderService.createOrderForCart(user));
    }

    @Test
    public void createOrderForCart_quantitiesDoesNotMatchStock_shouldNotChangeProductStock(){
        cartService.addToCart(user.getCart(), product_2, 2);
        cartService.addToCart(user.getCart(), product_1, 3);

        // Another user already buys the entire stock of product_2
        AppUser user2 = seeder.generateUser(AppUserRole.USER);
        userRepository.saveAndFlush(user2);

        cartService.addToCart(user2.getCart(), product_2, PRODUCT_2_QUANTITY);
        orderService.createOrderForCart(user2);

        Product product_1_db = productRepository.findById(product_1.getId()).get();
        Product product_2_db = productRepository.findById(product_2.getId()).get();

        assertThrows(QuantityCriteriaNotMetException.class,  () -> orderService.createOrderForCart(user));
        assertEquals(PRODUCT_1_QUANTITY, product_1_db.getStock());
        assertEquals(0, product_2_db.getStock());
    }
}
