package pl.allegrov2.allegrov2.services.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.allegrov2.allegrov2.data.dto.UserDetailsBasicDto;
import pl.allegrov2.allegrov2.data.dto.UserDetailsEmailDto;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.repositories.UserPaginatedRepository;
import pl.allegrov2.allegrov2.repositories.UserRepository;
import pl.allegrov2.allegrov2.services.mapping.MappingService;
import pl.allegrov2.allegrov2.validation.exceptions.MismatchException;
import pl.allegrov2.allegrov2.validation.exceptions.NotFoundException;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserPaginatedRepository paginatedRepository;
    private final MappingService mapper;
    private final BCryptPasswordEncoder encoder;

    public AppUser getUser(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Bad token"));
    }

    public AppUser getUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No user with id " + id));
    }

    public Page<AppUser> getUserPage(Pageable pageable){
        return paginatedRepository.findAll(pageable);
    }

    public UserDetailsEmailDto getUserDetails(String email){
        return mapper.convertToDto(getUser(email));
    }

    public void updateDetails(AppUser user, UserDetailsBasicDto newDetails){
        // fixme remove ability to change email

        user.updateDetails(newDetails);
        userRepository.save(user);
    }

    public void updatePassword(AppUser user, String oldPassword, String newPassword){

        if(!encoder.matches(oldPassword, user.getPassword()))
            throw new MismatchException("Old password does not equal new password");

        user.setPassword(encoder.encode(newPassword));

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("User with email: " + username + " not found."));
    }
}
