package pl.allegrov2.allegrov2.services.mapping;

import pl.allegrov2.allegrov2.data.dto.AddressDto;
import pl.allegrov2.allegrov2.data.dto.RegistrationDto;
import pl.allegrov2.allegrov2.data.dto.TokenDto;
import pl.allegrov2.allegrov2.data.dto.UserDetailsEmailDto;
import pl.allegrov2.allegrov2.data.entities.Address;
import pl.allegrov2.allegrov2.data.entities.AppUser;
import pl.allegrov2.allegrov2.data.enums.AppUserRole;

public interface MappingService {

    AppUser convertToEntity(RegistrationDto userDto, String pwdHashAndSalt, AppUserRole role, boolean enabled, boolean locked);

    AddressDto convertToDto(Address address);

    UserDetailsEmailDto convertToDto(AppUser appUser);

    TokenDto convertToDto(AppUser user, String token);
}
