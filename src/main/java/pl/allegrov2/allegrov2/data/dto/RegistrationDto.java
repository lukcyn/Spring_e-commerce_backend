package pl.allegrov2.allegrov2.data.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String email;   // TODO validate email

    @NotNull(message = "Phone number is mandatory")
    @Min(value = 1, message = "Phone number must be non-negative") // TODO validate phone number
    private Integer phoneNumber;

    private AddressDto address;
}
