package pl.allegrov2.allegrov2.services.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.allegrov2.allegrov2.data.dto.UserDetailsBasicDto;
import pl.allegrov2.allegrov2.data.dto.UserDetailsEmailDto;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.validation.exceptions.MismatchException;

public interface UserService extends UserDetailsService {

    AppUser getUser(String email);

    AppUser getUser(Long id);

    Page<AppUser> getUserPage(Pageable pageable);

    /**
     * @return user personal data. Does not return password.
     * */
    UserDetailsEmailDto getUserDetails(String email);

    /**
     *  Updates details fields from AppUser and saves them in database.
     *  Checks whether user with given email already exists.
     *  @throws MismatchException if he does.
     * */
    void updateDetails(AppUser user, UserDetailsBasicDto newDetails);

    /**
     *  Updates user password if current password is provided.
     * */
    void updatePassword(AppUser user, String oldPassword, String newPassword);
}
