package pl.allegrov2.allegrov2.data.seeding;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.allegrov2.allegrov2.data.entities.Address;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.entities.Photo;
import pl.allegrov2.allegrov2.data.entities.Product;
import pl.allegrov2.allegrov2.data.entities.cart.Cart;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class Seeder {
    public final static List<String> EXAMPLE_BRAND_NAMES = initBrands();
    private final static String PASSWORD = "Password";

    private final Faker faker;
    private final BCryptPasswordEncoder encoder;

    private final Random random = new Random();

    public AppUser generateUser(AppUserRole role, String email){
        Cart cart = Cart.builder()
                .cartItems(new ArrayList<>())
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

    public AppUser generateUser(AppUserRole role){
        return generateUser(role, faker.internet().emailAddress());
    }

    public List<AppUser> generateUsers(int amount){
        if(amount <= 0)
            amount = 1;

        return IntStream.rangeClosed(1, amount)
                .mapToObj(i -> generateUser(AppUserRole.USER))
                .toList();
    }

    public Product generateProduct(List<String> brandNames, long stock){
        Product product = new Product(
                null,
                brandNames.get(random.nextInt(0, brandNames.size())),
                faker.commerce().productName(),
                BigDecimal.valueOf(random.nextFloat(1.0f, 1000.0f)),
                stock
        );

        Photo photo = new Photo(faker.internet().url(), product);
        List<Photo> photos = new ArrayList<>();
        photos.add(photo);

        product.setPhotos(new ArrayList<>(photos));

        return product;
    }

    public Product generateProduct(List<String> brandNames){
        return generateProduct(brandNames, random.nextLong(0, 10));
    }

    public List<Product> generateProducts(int amount, List<String> brandNames){
        if(amount <= 0)
            amount = 1;

        return IntStream.rangeClosed(0, amount)
                .mapToObj(i -> generateProduct(brandNames))
                .toList();
    }

    private static List<String> initBrands(){
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
}
