package pl.allegrov2.allegrov2.data.dto.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AddressDto {

    @NotBlank(message = "Street name is mandatory")
    @NotNull
    private String streetName;

    @Min(1)
    private int streetNumber;

    private int residenceNumber;

    @NotBlank(message = "Zipcode is mandatory")
    @NotNull
    private String zipcode;
}
