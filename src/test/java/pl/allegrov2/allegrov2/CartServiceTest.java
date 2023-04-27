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
import pl.allegrov2.allegrov2.data.entities.cart.CartItem;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;
import pl.allegrov2.allegrov2.data.seeding.Seeder;
import pl.allegrov2.allegrov2.repositories.CartRepository;
import pl.allegrov2.allegrov2.repositories.ProductRepository;
import pl.allegrov2.allegrov2.repositories.UserRepository;
import pl.allegrov2.allegrov2.services.cart.CartService;
import pl.allegrov2.allegrov2.validation.exceptions.ResourceUnavailableException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(Transactional.TxType.NEVER)
public class CartServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private Seeder seeder;

    private AppUser user;
    private Product product;

    @BeforeEach
    public void init(){
        user = seeder.generateUser(AppUserRole.USER);
        product = seeder.generateProduct(List.of("BRAND1", "BRAND2", "BRAND3"), 5);

        user = userRepository.saveAndFlush(user);
        product = productRepository.saveAndFlush(product);
    }

    @AfterEach
    public void cleanUp(){
        userRepository.delete(user);
        productRepository.delete(product);
    }

    @Test
    public void addToCart_shouldSaveInDatabase(){
        cartService.addToCart(user.getCart(), product, 1);

        Cart cartFromDB = cartRepository.findById(user.getId()).get();
        assertTrue(user.getCart().getCartItems().containsAll(cartFromDB.getCartItems()));
    }

    @Test
    public void removeItem_itemShouldPersist(){
        cartService.addToCart(user.getCart(), product, 2);
        cartService.removeItem(user.getCart(), product, 1);

        Cart cartFromDB = cartRepository.findById(user.getId()).get();
        assertTrue(user.getCart().getCartItems().containsAll(cartFromDB.getCartItems()));
    }

    @Test
    public void removeItem_itemShouldBeRemoved(){
        cartService.addToCart(user.getCart(), product, 2);
        cartService.removeItem(user.getCart(), product, 2);

        Cart cartFromDB = cartRepository.getCartOfUser(user.getId()).get();
        List<CartItem> itemsFromDB = cartFromDB.getCartItems();

        assertTrue(itemsFromDB.isEmpty());
    }

    @Test
    public void addItem_addMoreThanInStock_shouldThrow(){
        assertThrows(
                ResourceUnavailableException.class,
                () -> cartService.addToCart(user.getCart(), product, 20)
        );
    }
}
