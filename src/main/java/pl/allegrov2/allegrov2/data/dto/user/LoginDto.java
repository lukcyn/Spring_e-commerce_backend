package pl.allegrov2.allegrov2.data.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@EqualsAndHashCode
public class LoginDto {

    @NotBlank(message = "Email is mandatory")
    @NotNull
    private String email;

    @NotBlank(message = "Password is mandatory")
    @NotNull
    private String password;
}
