package pl.allegrov2.allegrov2.data.dto.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@EqualsAndHashCode
public class RegistrationDto {

    @NotBlank(message = "Name is mandatory")
    @NotNull
    private String name;

    @NotBlank(message = "Surname is mandatory")
    @NotNull
    private String surname;

    @NotBlank(message = "Password is mandatory")
    @NotNull
    private String password;

    @NotBlank(message = "Email is mandatory")
    @NotNull
    private String email;

    @Min(1)
    private int phoneNumber;

    @NotNull(message = "Address is mandatory")
    @NotNull
    private AddressDto address;
}
