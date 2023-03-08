package pl.allegrov2.allegrov2.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.allegrov2.allegrov2.data.dto.AddressDto;
import pl.allegrov2.allegrov2.data.dto.RegistrationDto;
import pl.allegrov2.allegrov2.data.dto.TokenDto;
import pl.allegrov2.allegrov2.data.dto.UserDetailsEmailDto;
import pl.allegrov2.allegrov2.data.entities.Address;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;

@Service
@AllArgsConstructor
public class MappingService {

    private ModelMapper modelMapper;

    //TODO: unit tests
    public AppUser convertToEntity(RegistrationDto userDto,
                                   String pwdHashAndSalt,
                                   AppUserRole role,
                                   boolean enabled,
                                   boolean locked){
        AppUser userEntity = modelMapper.map(userDto, AppUser.class);
        userEntity.setPassword(pwdHashAndSalt);
        userEntity.setRole(role);
        userEntity.setEnabled(enabled);
        userEntity.setLocked(locked);
        return userEntity;
    }

    public AddressDto convertToDto(Address address){
        return modelMapper.map(address, AddressDto.class);
    }

    public UserDetailsEmailDto convertToDto(AppUser appUser){
        UserDetailsEmailDto userDetails = modelMapper.map(appUser, UserDetailsEmailDto.class);
        userDetails.setAddress(convertToDto(appUser.getAddress()));

        return userDetails;
    }

    public TokenDto convertToDto(AppUser user, String token){
        return new TokenDto(user.getEmail(), token);
    }
}
