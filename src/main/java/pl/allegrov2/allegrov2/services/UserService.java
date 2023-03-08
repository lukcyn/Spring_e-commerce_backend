package pl.allegrov2.allegrov2.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.allegrov2.allegrov2.data.dto.UserDetailsBasicDto;
import pl.allegrov2.allegrov2.data.dto.UserDetailsEmailDto;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.repositories.IUserPaginatedRepository;
import pl.allegrov2.allegrov2.repositories.IUserRepository;
import pl.allegrov2.allegrov2.validation.exceptions.MismatchException;
import pl.allegrov2.allegrov2.validation.exceptions.NotFoundException;

@Service
@AllArgsConstructor
public class UserService {

    private final IUserRepository userRepository;
    private final IUserPaginatedRepository paginatedRepository;
    private final MappingService mapper;
    private final BCryptPasswordEncoder encoder;


    /**
     * Gets user from database by matching email.
     * */
    public AppUser getUser(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Bad token"));
    }

    /**
     * Gets user from database by id.
     * */
    public AppUser getUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No user with id " + id));
    }

    public Page<AppUser> getUserPage(Pageable pageable){
        return paginatedRepository.findAll(pageable);
    }

    /**
     * Return user personal data. Does not return password.
     * Gets the data by matching emails.
     * */
    public UserDetailsEmailDto getUserDetails(String email){
        return mapper.convertToDto(getUser(email));
    }

    /**
     *  Updates details fields from AppUser and saves them in database.
     *  Checks whether user with given email already exists. Throws exception
     *  if he does.
     * */
    public void updateDetails(AppUser user, UserDetailsBasicDto newDetails){
        // fixme remove ability to change email

        user.updateDetails(newDetails);
        userRepository.save(user);
    }

    /**
     *  Updates user password if current password is provided.
     * */
    public void updatePassword(AppUser user, String oldPassword, String newPassword){

        if(!encoder.matches(oldPassword, user.getPassword()))
            throw new MismatchException("Old password does not equal new password");

        user.setPassword(encoder.encode(newPassword));

        userRepository.save(user);
    }


}
