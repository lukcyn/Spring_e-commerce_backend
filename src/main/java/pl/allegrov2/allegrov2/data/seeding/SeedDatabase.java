package pl.allegrov2.allegrov2.data.seeding;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;
import pl.allegrov2.allegrov2.repositories.ProductRepository;
import pl.allegrov2.allegrov2.repositories.UserRepository;

import java.util.*;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class SeedDatabase{

    private final static int PRODUCTS_COUNT = 1000;
    private final static int USERS_COUNT = 100;
    private final static String PASSWORD = "Password";
    private final Logger log = LoggerFactory.getLogger(SeedDatabase.class);

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final Seeder seeder;


    @Bean
    public CommandLineRunner seedData(){
        return args -> seedDatabase();
    }

    private void seedDatabase(){
        log.debug("Seeding database");

        if(userRepository.count() <= 0){
            log.debug("Seeding users: ");

            //Adding admin account with known email:
            userRepository.save(seeder.generateUser(AppUserRole.ADMIN, "randomAdmin@random.com"));

            //Adding user account with known email
            userRepository.save(seeder.generateUser(AppUserRole.USER, "randomUser@random.com"));

            //Adding normal users
            List<AppUser> users = seeder.generateUsers(USERS_COUNT);
            userRepository.saveAll(users);
        }

        if(productRepository.count() <= 0){
            log.debug("Seeding products: ");

            List<Product> products = seeder.generateProducts(PRODUCTS_COUNT, Seeder.EXAMPLE_BRAND_NAMES);
            productRepository.saveAll(products);
        }
        log.debug("Seeding completed");
    }
}
