package pl.allegrov2.allegrov2.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TokenDto {

    @NotBlank(message = "email field is required")
    private String email;

    @NotBlank(message = "token field is required")
    private String token;
}
