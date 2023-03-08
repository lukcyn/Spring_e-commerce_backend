package pl.allegrov2.allegrov2.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AddressDto {

    @NotBlank(message = "streetName field is required")
    private String streetName;

    @NotNull
    private Integer streetNumber;

    private Integer residenceNumber;

    @NotBlank(message = "zipcode field is required")
    private String zipcode;
}
