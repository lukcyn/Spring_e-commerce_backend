package pl.allegrov2.allegrov2.data.seeding;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.allegrov2.allegrov2.data.entities.Address;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.entities.Photo;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;
import pl.allegrov2.allegrov2.repositories.ProductRepository;
import pl.allegrov2.allegrov2.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class SeedDatabase{

    private final static int PRODUCTS_COUNT = 1000;
    private final static int USERS_COUNT = 100;
    private final static String PASSWORD = "Password";


    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final Logger log = LoggerFactory.getLogger(SeedDatabase.class);

    private final Faker faker;
    private final BCryptPasswordEncoder encoder;

    private final Random random = new Random();

    // Temporary data for product generation
    private final List<String> brandNames = initBrands();

    @Bean
    public CommandLineRunner seedData(){

        //todo check if in development and whether database contains information
        return args -> seedDatabase();
    }

    private void seedDatabase(){
        log.debug("Seeding database");

        if(userRepository.count() <= 0){
            log.debug("Seeding users: ");

            //Adding admin account:
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
        AppUser user = new AppUser(
                faker.name().name(),
                faker.name().lastName(),
                email,
                886432261,
                role,
                false,
                true,
                encoder.encode(PASSWORD),
                new Address(
                        faker.address().streetName(),
                        Integer.parseInt(faker.address().streetAddressNumber()),
                        Integer.parseInt(faker.address().streetAddressNumber()),
                        faker.address().zipCode(),
                        null
                ));
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
                new BigDecimal(random.nextFloat(1.0f, 1000.0f)),
                random.nextInt(0, 10)
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
