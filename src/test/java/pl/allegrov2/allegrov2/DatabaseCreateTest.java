package pl.allegrov2.allegrov2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.allegrov2.allegrov2.data.entities.Address;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;
import pl.allegrov2.allegrov2.repositories.IUserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DatabaseCreateTest {

    @Autowired
    private IUserRepository userRepository;

    @Test
    public void givenUser_whenSave_thenGetOk() {
        Address address = new Address();
        address.setStreetName("d");
        address.setStreetNumber(12);
        address.setResidenceNumber(1);
        address.setZipcode("da");

        AppUser user = new AppUser();
        user.setName("john");
        user.setEmail("email");
        user.setEnabled(true);
        user.setLocked(false);
        user.setPassword("password");
        user.setPhoneNumber(123123);
        user.setRole(AppUserRole.USER);
        user.setSurname("surname");
        user.setAddress(address);

        address.setUser(user);

        userRepository.save(user);

        AppUser user2 = userRepository.findById(1L).orElseThrow();
        assertEquals("john", user2.getName());
    }
}
