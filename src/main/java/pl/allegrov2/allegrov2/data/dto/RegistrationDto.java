package pl.allegrov2.allegrov2.data.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@EqualsAndHashCode
public class RegistrationDto {

    @NotBlank(message = "name field is required")
    private String name;

    @NotBlank(message = "surname field is required")
    private String surname;

    @NotBlank(message = "password field is required")
    private String password;

    @NotBlank(message = "email field is required")
    @Email(message = "email is not valid")
    private String email;

    @NotNull(message = "Phone number is mandatory")
    @Pattern(regexp="(^$|[0-9]{10})", message = "Invalid phone number")
    private String phoneNumber;

    private AddressDto address;
}
