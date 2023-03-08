package pl.allegrov2.allegrov2.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsBasicDto {

    private Long id;

    @NotBlank(message = "name field is mandatory")
    private String name;

    @NotBlank(message = "surname field is required")
    private String surname;

    @NotNull(message = "phoneNumber field is mandatory")
    private Integer phoneNumber;

    @NotNull(message = "address field is mandatory")
    private AddressDto address;
}
