package pl.allegrov2.allegrov2.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@EqualsAndHashCode
public class LoginDto {

    @NotBlank(message = "email field is required")
    private String email;

    @NotBlank(message = "password field is required")
    private String password;
}
