package pl.allegrov2.allegrov2.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor @AllArgsConstructor
public class UserDetailsEmailDto extends UserDetailsBasicDto {

    @NotBlank(message = "email field is required")
    private String email;
}
