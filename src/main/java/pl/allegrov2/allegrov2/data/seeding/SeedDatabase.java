package pl.allegrov2.allegrov2.data.seeding;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.allegrov2.allegrov2.data.entities.Address;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.entities.Photo;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.data.entities.cart.Cart;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;
import pl.allegrov2.allegrov2.repositories.ProductRepository;
import pl.allegrov2.allegrov2.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.IntStream;

@Configuration
@Profile("dev")
public class SeedDatabase{

    private final static int PRODUCTS_COUNT = 1000;
    private final static int USERS_COUNT = 100;
    private final static String PASSWORD = "Password";

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final BCryptPasswordEncoder encoder;

    private final Logger log;
    private final Faker faker;
    private final Random random;

    public SeedDatabase(UserRepository userRepository,
                        ProductRepository productRepository,
                        BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.encoder = encoder;

        this.faker = new Faker();
        this.random = new Random();
        this.log = LoggerFactory.getLogger(SeedDatabase.class);
    }

    // Samples of brand names
    private final List<String> brandNames = initBrands();

    @Bean
    public CommandLineRunner seedData(){
        return args -> seedDatabase();
    }

    private void seedDatabase(){
        log.debug("Seeding database");

        if(userRepository.count() <= 0){
            log.debug("Seeding users: ");

            //Adding admin account with known email:
            userRepository.save(generateUser(AppUserRole.ADMIN, "randomAdmin@random.com"));

            //Adding user account with known email
            userRepository.save(generateUser(AppUserRole.USER, "randomUser@random.com"));

            //Adding normal users
            List<AppUser> users = generateUsers(USERS_COUNT);
            userRepository.saveAll(users);
        }

        if(productRepository.count() <= 0){
            log.debug("Seeding products: ");

            List<Product> products = generateProducts(PRODUCTS_COUNT);
            productRepository.saveAll(products);
        }
        log.debug("Seeding completed");
    }


    private List<String> initBrands(){
        ArrayList<String> brands = new ArrayList<>();

        brands.add("Adidas");
        brands.add("Nike");
        brands.add("Rebook");
        brands.add("Nokia");
        brands.add("Samsung");
        brands.add("Apple");
        brands.add("Lenovo");
        brands.add("Asus");
        brands.add("Acer");

        return brands;
    }

    private List<AppUser> generateUsers(int amount){
        if(amount <= 0)
            amount = 1;

        return IntStream.rangeClosed(1, amount)
                .mapToObj(i -> generateUser(AppUserRole.USER))
                .toList();
    }

    private AppUser generateUser(AppUserRole role, String email){
        Cart cart = Cart.builder()
                .build();


        AppUser user = AppUser.builder()
                .name(faker.name().name())
                .surname(faker.name().lastName())
                .email(email)
                .phoneNumber(886432261)
                .role(role)
                .enabled(true)
                .locked(false)
                .password(encoder.encode(PASSWORD))
                .address(Address.builder()
                        .streetName(faker.address().streetName())
                        .streetNumber(Integer.parseInt(faker.address().streetAddressNumber()))
                        .residenceNumber(Integer.parseInt(faker.address().streetAddressNumber()))
                        .zipcode(faker.address().zipCode())
                        .build())
                .cart(cart)
                .build();

        cart.setUser(user);

        user.getAddress().setUser(user);

        return user;
    }

    private AppUser generateUser(AppUserRole role){
        return generateUser(role, faker.internet().emailAddress());
    }

    private Product generateProduct(){
        Product product = new Product(
                null,
                brandNames.get(random.nextInt(0, brandNames.size())),
                faker.commerce().productName(),
                BigDecimal.valueOf(random.nextFloat(1.0f, 1000.0f)),
                random.nextLong(0, 10)
        );

        Photo photo = new Photo(faker.internet().url(), product);
        List<Photo> photos = new ArrayList<>();
        photos.add(photo);

        product.setPhotos(new ArrayList<>(photos));

        return product;
    }

    private List<Product> generateProducts(int amount){
        if(amount <= 0)
            amount = 1;

        return IntStream.rangeClosed(0, amount)
                .mapToObj(i -> generateProduct())
                .toList();
    }
}
